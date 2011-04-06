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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
* Used for generally purpose utility-methods
*/
public final class Util implements Constants {

    private final static boolean DEBUG = true;
    private static Properties properties = null;

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
     * Returns Configurationparametres from a centralized app.properties file
     *
     * @param key the key-string of the property
     * @return the property value
     */
    public static String getProperty(String key) {
        if(properties == null) {
            properties = new Properties();
            BufferedInputStream stream;
            try {
                stream = new BufferedInputStream(new FileInputStream("config/app.properties"));
                properties.load(stream);
                stream.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return properties.getProperty(key);
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
