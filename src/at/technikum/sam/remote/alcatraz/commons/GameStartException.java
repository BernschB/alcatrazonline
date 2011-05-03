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
 * Exception that indicates that the start of the Game has failed
 */
public class GameStartException extends Exception {
    /**
     * Constructor
     * @param message 
     */
    public GameStartException(String message) {
        super(message);
    }

}
