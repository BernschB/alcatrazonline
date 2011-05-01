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
 *
 * TODO: Für was brauchen wir die???
 */
public class NotMasterException extends Exception {
    private String host;
    private int port;
    
    public NotMasterException(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
}
