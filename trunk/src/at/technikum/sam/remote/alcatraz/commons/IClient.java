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

package at.technikum.sam.remote.alcatraz.commons;

import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * ICLient.java
 * this Interface contains all the methods the server needs to communicate with
 * the clients and all the methods the clients need to communicate with each other
 * 
 */
public interface IClient extends Remote {
    
    /**
     * is called by the master server right after it was elected new master
     * the new master calls this on all registered clients
     * 
     * @param host hostname of the new master
     * @param port portnumber of the new master
     * @return true if successfully finished
     * @throws RemoteException 
     */
    boolean reportNewMaster(String host, int port)
            throws RemoteException;

    /**
     * is called by server or other clients to test if client is still alive
     * and prints a debug message if debugging is activated
     * 
     * @param callerName the name of the caller
     * @return true if client is alive
     * @throws RemoteException 
     */
    boolean debugIsAlive(String callerName)
            throws RemoteException;
    
    /**
     * is called by server or other clients to test if client is still alive
     * 
     * @return true if client is alive
     * @throws RemoteException 
     */
    boolean isAlive()
            throws RemoteException;
    
    /**
     * the server calls this method on all the clients if a game needs to be
     * started - the clients get a list of all the players registered for the
     * game and start their GUIs
     * 
     * @param players
     * @return true if successfully finished
     * @throws GameStartException
     * @throws RemoteException 
     */
    boolean startGame(List<PlayerAdapter> players)
            throws GameStartException, RemoteException;
    
    /**
     * if a client makes a move in the alcatraz game it uses this method to
     * distribute its move to all the other clients
     * 
     * @param player the player which moved
     * @param prisoner the prisoner which was moved
     * @param rowOrCol 
     * @param row
     * @param col
     * @return true if successfully finished
     * @throws RemoteException 
     */
    boolean doMove(Player player, Prisoner prisoner,  int rowOrCol, int row, int col)
            throws RemoteException;

    /**
     * is called on all other clients if a player is absent
     * 
     * @param player the player which is absent
     * @throws RemoteException 
     */
    void playerAbsent(PlayerAdapter player)
            throws RemoteException;

}
