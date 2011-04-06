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
     * Timeout to wait between retries if move propagation fails
     */
    public final static int TIMEOUT = 6000;

    /**
     * RMI Object service name for IClients
     */
    public final static String RMI_CLIENT_SERVICE
            = "ClientService";

    /**
     * RMI Object service name for IServers
     */
    public final static String RMI_SERVER_SERVICE 
            = "RegistryService";

    /**
     * Spread Server Group Name
     */
    public final static String SPREAD_SERVER_GROUP_NAME
            = "AlcatrazRegistry";
    /**
     * Exception Message #1 for GameRegistryException
     */
    public final static String EX_MSG_GAME_REGISTRY_FAILED
            = "Registration of player {0} failed";

    /**
     * Property Key for Registry Server Hostname property
     */
    public final static String CONF_REGISTRYSERVERHOSTNAME
            = "RegistryServerHostname";

    /**
     * Property Key for Private Spread Group property
     */
    public final static String CONF_PRIVATESPREADGROUP
            = "PrivateSpreadGroup";
}
