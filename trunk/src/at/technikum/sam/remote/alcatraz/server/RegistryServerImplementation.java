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

import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.IRegistryServer;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * TODO: Comment
 */
public class RegistryServerImplementation extends UnicastRemoteObject
        implements IRegistryServer, Serializable {
    
    public RegistryServerImplementation() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PlayerAdapter createPlayer(String name) throws NameAlreadyInUseException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void register(PlayerAdapter player) throws GameRegistryException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unregister(PlayerAdapter player) throws GameRegistryException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void forceStart(PlayerAdapter player) throws GameStartException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
