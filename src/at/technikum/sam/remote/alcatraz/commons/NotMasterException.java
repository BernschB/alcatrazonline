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

/**
 * NotMasterExpetion.java
 * 
 * is thrown when a client calls a method on a server which is currently not 
 * a master server
 */
public class NotMasterException extends Exception {
    private String host;
    private int port;
    
    /**
     * 
     * 
     * @param host the hostname of the current master server
     * @param port the portnumber of the current master server
     */
    public NotMasterException(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    /**
     * 
     * @return hostname of the current master server
     */
    public String getHost() {
        return this.host;
    }
    /**
     * 
     * @return portnumber of the current master server 
     */
    public int getPort() {
        return this.port;
    }
}
