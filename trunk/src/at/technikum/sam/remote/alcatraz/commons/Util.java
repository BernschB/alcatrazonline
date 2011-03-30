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
* Used for generally purpose utility-methods
*/
public final class Util implements Constants {

    private final static boolean DEBUG = true;


    private static int sequencer = -1;

    private Util() {

    }

    /**
     * Used for printng debug messages
     *
     * @param message the debug message
     */
    public static void printDebug(String message) {
        if(DEBUG) {
            System.out.println(message);
        }
    }

    /**
     * Returns increasing int values at every call beginning with 0
     *
     * @return An int value sequence
     */
    public static int getSequence() {
        return sequencer++;
    }
}
