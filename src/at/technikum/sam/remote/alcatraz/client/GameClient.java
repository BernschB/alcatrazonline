/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.technikum.sam.remote.alcatraz.client;

import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.IRegistryServer;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 *
 * @author Sebastian
 */
public class GameClient {

  public final static String HOST = "localhost";
  public final static int PORT = 1099;
  
  private static ClientImplementation myClient = null;
  private static PlayerAdapter myPlayer = null;

  public static void main(String[] args) {


      try {
          myClient = new ClientImplementation(HOST, PORT);
      } catch (Exception ex) {
          //TODO: ExceptionHandling
          ex.printStackTrace();
      }


      try {
          myPlayer = myClient.getMasterServer().createPlayer("Bob");
      } catch (NameAlreadyInUseException ex) {
          ex.printStackTrace();
      } catch (RemoteException ex) {
          ex.printStackTrace();
      } catch (Exception ex) {
          ex.printStackTrace();
      }

      try {
        myClient.getMasterServer().register(new PlayerAdapter(myPlayer, myClient));
      } catch (GameRegistryException ex) {
          ex.printStackTrace();
      } catch (RemoteException ex) {
          ex.printStackTrace();
      }






  }



}
