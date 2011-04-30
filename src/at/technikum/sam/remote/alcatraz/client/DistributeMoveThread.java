/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.sam.remote.alcatraz.client;

import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.Move;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.rmi.RemoteException;

/**
 *
 * @author Sebastian_2
 */
public class DistributeMoveThread extends Thread {

    private ClientImplementation client;
    private Move move;

    public DistributeMoveThread(ClientImplementation client, Move move) {
        this.client = client;
        this.move = move;
    }

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
                    Thread.sleep(Constants.TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                success = false;
            }
        }
    }
}
