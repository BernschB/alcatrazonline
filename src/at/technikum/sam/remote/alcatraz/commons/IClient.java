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
 *
 * TODO: Comment
 */
public interface IClient extends Remote {

    void reportNewMaster(String host, int port)
            throws RemoteException;

    boolean isAlive()
            throws RemoteException;
    
    boolean startGame(List<PlayerAdapter> players)
            throws GameStartException, RemoteException;

    void doMove(Player player, Prisoner prisoner,  int rowOrCol, int row, int col)
            throws MoveException, RemoteException;

    void yourTurn()
            throws RemoteException;

    void playerAbsent(PlayerAdapter player)
            throws RemoteException;

}
