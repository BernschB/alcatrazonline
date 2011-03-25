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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import spread.AdvancedMessageListener;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

/**
 *
 * TODO: comment
 */
public class RegistryServer implements AdvancedMessageListener, Constants {

    private static String spreadHost = "localhost";

    private SpreadConnection connection;
    private SpreadGroup group;

    public static void main (String[] args) {
        new RegistryServer();
    }

    public RegistryServer() {
        try {
            System.out.println("Joining Spread Server Group...");
            try
            {
                    /* Connect to local spread daemon and get spread group */
                    connection = new SpreadConnection();
                    connection.connect( InetAddress.getByName(spreadHost),
                                        0,
                                        SPREAD_SERVER_GROUP_NAME,
                                        true,
                                        true);

                    /* Add AdvancedMessageListener Implementation to this Spread connection */
                    connection.add(this);
            }
            catch(SpreadException e)
            {
                    System.err.println("There was an error connecting to the daemon.");
                    e.printStackTrace();
                    System.exit(1);
            }
            catch(UnknownHostException e)
            {
                    System.err.println("Can't find the spread daemon on" + spreadHost);
                    System.exit(1);
            }

            System.out.println("Starting up Registry Server instance...");
            RegistryServerImplementation r = new RegistryServerImplementation();
            System.out.println("Creating Registry...");
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/"
                    .concat(RMI_SERVER_SERVICE), r);
            System.out.println("Registry Server up and running.");
        } catch (Exception e) {
            System.out.println("Something went wrong while bringing up server.");
            e.printStackTrace();
        }
    }

    public void regularMessageReceived(SpreadMessage sm) {
        throw new UnsupportedOperationException("Not supported yet.");

        /* Blabhdhfod */
        
        /**
         * TODO: Implement Server state synchronisation here
         */
    }

    public void membershipMessageReceived(SpreadMessage sm) {
        throw new UnsupportedOperationException("Not supported yet.");

        /* comment */
        
        /**
         * TODO: Implement group join/leave here
         */

        /**
         * TODO: Implement master server failover here
         */
    }

}
