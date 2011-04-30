/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.technikum.sam.remote.alcatraz.client;

import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.Player;

/**
 *
 * TODO: Comment
 */
public interface ClientListener {

    void gameStarted (Alcatraz game);

    void gameWon(Player player);
    
    void playerAbsent(String name);

}
