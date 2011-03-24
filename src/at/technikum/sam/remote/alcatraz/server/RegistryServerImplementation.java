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

import at.falb.games.alcatraz.api.Player;
import at.technikum.sam.remote.alcatraz.client.ClientImplementation;
import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.IRegistryServer;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import at.technikum.sam.remote.alcatraz.commons.Constants;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TODO: Comment
 */
public class RegistryServerImplementation extends UnicastRemoteObject
        implements IRegistryServer, Serializable, Constants {

    private static Game currentGame = null;
    
    public RegistryServerImplementation() throws RemoteException {
        currentGame = new Game();
        currentGame.Reset();
    }

    /**
     * Creates a new player object with an unique player ID and the
     * given name and associates it with the requesters stub
     *
     * @param name the new players nickname
     * @return a PlayerAdapter object containing the player and its stub or
     * NULL if no player could be created
     * @throws NameAlreadyInUseException if the given is already used
     * @throws RemoteException if the associated client stub cannot be reached
     */
    public PlayerAdapter createPlayer(String name)
            throws NameAlreadyInUseException, RemoteException {
        
        if(nameInUse(name)) {
            throw new NameAlreadyInUseException();
        }

        Player newPlayer = new Player(Game.getNewPlayerId());
        newPlayer.setName(name);
        
        ClientImplementation client = null;

        try {
            client = (ClientImplementation) Naming
                .lookup("rmi:/"
                .concat(getClientHost())
                .concat("/")
                .concat(Constants.RMI_CLIENT_SERVICE));
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(client != null) {
             return new PlayerAdapter(newPlayer, client);
        } else {
            return null;
        }
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

    private static boolean nameInUse(String name) {
        Vector<PlayerAdapter> v = currentGame.getPlayers();
        PlayerAdapter pa = null;
        Iterator i = v.iterator();

        while(i.hasNext()) {
            pa = (PlayerAdapter)i.next();
            if(pa.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }
}
