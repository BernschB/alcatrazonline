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

import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.IClient;
import at.technikum.sam.remote.alcatraz.commons.MoveException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * TODO: Comment
 */
public class ClientImplementation extends UnicastRemoteObject implements IClient, Serializable {
    
    public ClientImplementation () throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reportNewMaster(String host, int port) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAlive() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean startGame(List<PlayerAdapter> players) throws GameStartException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doMove(Player player, Prisoner prisoner, int rowOrCol, int row, int col) throws MoveException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void yourTurn() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void playerAbsent(PlayerAdapter player) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
