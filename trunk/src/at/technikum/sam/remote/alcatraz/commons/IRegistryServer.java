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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * TODO: Comment
 */
public interface IRegistryServer extends Remote {

    /*
     * TODO: könnten wir da nicht gleich dem Server einen ClientStub mitgeben?
     * Dann würden wir uns das lookup (serverseitig) sparen und eventuell auch
     * die register Methode...
     */

    void register(PlayerAdapter player)
            throws NameAlreadyInUseException, ClientAlreadyRegisteredException, GameRegistryException, RemoteException;

    void unregister(PlayerAdapter player)
            throws GameRegistryException, RemoteException;

    void forceStart(PlayerAdapter player)
            throws GameStartException, RemoteException;

}
