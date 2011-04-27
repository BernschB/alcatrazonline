/**
 * FH Technikum-Wien,
 * BICSS - Sommersemester 2011
 *
 * Softwarearchitekturen und Middlewaretechnologien
 * Alcatraz - Remote - Projekt
 * Gruppe B2
 *
 *
 * @author Christian Fossati
 * @author Stefan Schramek
 * @author Michael Strobl
 * @author Sebastian Vogel
 * @author Juergen Zornig
 *
 *
 * @date 2011/03/10
 *
 **/
package at.technikum.sam.remote.alcatraz.server;

import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

/**
 *
 * TODO: comment
 */
public class RegistryServer implements Constants {

  private static String host = "localhost";
  private static String port = "1099";
  private static SpreadConnection connection;
  private SpreadGroup group;

  public static void main(String[] args) {
      //System.setProperty("java.rmi.server.hostname", "10.201.93.184");
    new RegistryServer();
  }

  public RegistryServer() {
    try {
      /*System.out.println("Starting up Spread Daemon...");
      this.startSpread();*/
      host = Util.getProperty(CONF_REGISTRYSERVERHOSTNAME);
      port = Util.getProperty(CONF_REGISTRYSERVERPORT);
      System.out.println("Initializing Server on Host: "
              .concat(host)
              .concat("..."));

      System.out.println("Starting up Registry Server instance...");
      RegistryServerImplementation r = new RegistryServerImplementation();

      System.out.println("Joining Spread Server Group...");
      try {
        /* Connect to local spread daemon and get spread group */
        connection = new SpreadConnection();
        connection.connect(InetAddress.getByName(host),
                0,
               Util.getProperty(CONF_PRIVATESPREADGROUP),
                true,
                true);

        /* Add AdvancedMessageListener Implementation to this Spread connection */
        connection.add(r);

        /* Auto join group */
        group = new SpreadGroup();
        group.join(connection, SPREAD_SERVER_GROUP_NAME);
      } catch (SpreadException e) {
        System.err.println("There was an error connecting to the daemon.");
        e.printStackTrace();
        System.exit(1);
      } catch (UnknownHostException e) {
        System.err.println("Can't find the spread daemon on" + host);
        System.exit(1);
      }

      System.out.println("Creating RMI Registry...");
      java.rmi.registry.LocateRegistry.createRegistry(1099);
      Naming.rebind("rmi://"
              .concat(host)
              .concat(":")
              .concat(port)
              .concat("/")
              .concat(RMI_SERVER_SERVICE), r);
      System.out.println("Registry Server up and running. Ready to receive requests...");
    } catch (Exception e) {
      System.out.println("Something went wrong while bringing up server.");
      e.printStackTrace();
    }

    byte buffer[] = new byte[80];
    do {
      try {
        System.in.read(buffer);
        if (buffer[0] == 'j') {
          group = new SpreadGroup();
          group.join(connection, SPREAD_SERVER_GROUP_NAME);
        } else if (buffer[0] == 'l') {
          group.leave();
        } else if (buffer[0] == 'm') {
          SpreadMessage message = new SpreadMessage();
          //message.addGroup(group);
          message.setObject(new String("Testing Multicast"));
          connection.multicast(message);
        } else if (buffer[0] == 'q') {
          group.leave();
          System.exit(0);
        }
      } catch (Exception e) {
        System.out.println("Something went wrong while bringing up server.");
        e.printStackTrace();
      }
    } while (buffer[0] != 'q');
  }

  public static SpreadConnection getSpreadConnection() {
    return connection;
  }

  /**
   * TODO: Spread Startup Method... Work in Progress
   */
  private void startSpread() {
    ProcessBuilder spreadDaemon = new ProcessBuilder("spread -c spread.conf -n host1");

    spreadDaemon.directory(new File(System.getProperty("user.dir").toString()));
    Util.printDebug(System.getProperty("user.dir"));
    try {
      spreadDaemon.start();
    } catch (IOException ex) {
      Logger.getLogger(RegistryServer.class.getName()).log(Level.SEVERE, null, ex);
      Util.printDebug(ex.toString());
    }
  }
}
