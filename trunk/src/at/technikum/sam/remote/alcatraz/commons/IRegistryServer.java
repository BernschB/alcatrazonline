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

    PlayerAdapter createPlayer(String name)
            throws NameAlreadyInUseException, RemoteException;

    void register(PlayerAdapter player)
            throws GameRegistryException, RemoteException;

    void unregister(PlayerAdapter player)
            throws GameRegistryException, RemoteException;

    void forceStart(PlayerAdapter player)
            throws GameStartException, RemoteException;

}
