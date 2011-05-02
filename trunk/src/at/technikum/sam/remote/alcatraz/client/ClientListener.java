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

package at.technikum.sam.remote.alcatraz.client;

import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.Player;

/**
 * ClientListener.java
 * 
 * with this interface it is possible to listen on certain events within the 
 * ClientImplementation
 */
public interface ClientListener {
    
    
    /**
     * is called by the ClientImplementation if the game was started by the Server
     * 
     * @param game the Alcatraz game that was started 
     */
    void gameStarted (Alcatraz game);
    
    /**
     * is called by the ClientImplementation if a player has won the game
     * 
     * @param player the player which won
     */
    void gameWon(Player player);
    
    /**
     * is called by the ClientImplementation if another player was reported 
     * absent
     * 
     * @param name the name of the absent player
     */
    void playerAbsent(String name);

}
