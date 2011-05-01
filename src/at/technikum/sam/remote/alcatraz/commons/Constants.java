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
     * Maximum number of retries when when trying to reach client
     */
    public final static int MAXRETRIES = 10;

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
            = Util.getProperty("RegistryServerHostname");

    /**
     * Property Key for Private Spread Group property
     */
    public final static String CONF_PRIVATESPREADGROUP
            = Util.getProperty("PrivateSpreadGroup");
    
    /**
     * Property KEy for Registry Server Por
     */
    public final static String CONF_REGISTRYSERVERPORT
            = Util.getProperty("RegistryServerPort");
    /**
     * Error Message displayed to the user if Player Name is already in use
     */
    public final static String ERR_NAMEINUSE 
            = "Error: this name is already in use";
    
    /**
     * Error Message displayed to the user if not all Text-Fields are filled out
     */
    public final static String ERR_TEXTFIELDEMPTY 
            = "Error: please fill out all three text fields";
    
    /**
     * Error Message displayed to the user if server could not be reached
     */
    public final static String ERR_SERVERNOTREACHED
            = "Error: could noit reach server";
    
     /**
     * Error Message displayed to the user if an error occurs
     */
    public final static String ERR_ERROR
            = "Error: an error occured - please try again";
    
     /**
     * Error Message displayed to the user if not enough player are registered with the server
     */
    public final static String ERR_NOTENOUGHPLAYER
            = "Error: not enough player registered with server";
}
