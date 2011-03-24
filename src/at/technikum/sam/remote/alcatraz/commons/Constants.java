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
 * @date 2011/03/24
 *
 **/
package at.technikum.sam.remote.alcatraz.commons;

/**
 *
 * @author pinkerl
 */
public interface Constants {
    /**
     * Maximum Number of players allowed in a game
     */
    public final static int MAXPLAYERS = 4;

    /**
     * RMI Object service name for IClients
     */
    public final static String RMI_CLIENT_SERVICE = "ClientService";

    /**
     * RMI Object service name for IServers
     */
    public final static String RMI_SERVER_SERVICE = "ServerService";
}
