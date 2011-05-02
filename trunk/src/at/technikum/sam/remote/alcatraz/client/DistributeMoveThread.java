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

package at.technikum.sam.remote.alcatraz.client;

import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.Move;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DistributeMoveThread.java
 * 
 * is the  thread which is responsible for the distribution of a move
 * to all the other clients
 * 
 */
public class DistributeMoveThread extends Thread {

    private ClientImplementation client;
    private Move move;
    
    /**
     * Constructor
     * 
     * @param client the calling client
     * @param move the move that needs to be distributed
     */

    public DistributeMoveThread(ClientImplementation client, Move move) {
        this.client = client;
        this.move = move;
    }
    
    /**
     * starts the distribution
     */

    @Override
    public void run() {

        for (PlayerAdapter currentPlayer : client.getThePlayers()) {
            if (!(currentPlayer.equals(client.getMyPlayer())
                    || (currentPlayer.equals(client.getNextPlayer())))) {

                distributeMove(currentPlayer, move);

            }
        }

        distributeMove(client.getNextPlayer(), move);


    }

    private void distributeMove(PlayerAdapter currentPlayer, Move move) {
        boolean success = false;
        boolean playerAbsentCalled = false;
        int count = 0;

        while (!success && (count < Constants.MAXRETRIES)) {
            try {
                success = currentPlayer.getClientstub().
                        doMove(
                        move.getPlayer(),
                        move.getPrsnr(),
                        move.getRowOrCol(),
                        move.getRow(),
                        move.getCol());
            } catch (RemoteException ex) {

                try {
                    // call playerAbsent only once per absent player
                    if (!playerAbsentCalled) {
                        playerAbsentCalled = true;
                        for (PlayerAdapter otherPlayer : client.getThePlayers()) {
                            if (!otherPlayer.equals(currentPlayer)) {
                                otherPlayer.getClientstub().playerAbsent(currentPlayer);
                            }
                        }
                    }
                    count++;
      
                } catch (Exception e) {
                    Logger.getLogger(DistributeMoveThread.class.getName()).log(Level.INFO, null, ex);
                }
                Logger.getLogger(DistributeMoveThread.class.getName()).log(Level.FINE, null, ex);
                success = false;
            }
        }
    }
}
