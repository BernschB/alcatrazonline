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
 * Registry server interface
 */
public interface IRegistryServer extends Remote {

    void register(PlayerAdapter player)
            throws NameAlreadyInUseException, ClientAlreadyRegisteredException, GameRegistryException, RemoteException, NotMasterException;

    void unregister(PlayerAdapter player)
            throws GameRegistryException, RemoteException;

    void forceStart(PlayerAdapter player)
            throws GameStartException, RemoteException;

}
