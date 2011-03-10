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

import at.falb.games.alcatraz.api.Player;
import java.io.Serializable;

/**
 *
 * TODO: Comment
 */
public class PlayerAdapter extends Player implements Serializable{

    private IClient clientstub;

    public PlayerAdapter(Player player, IClient clientstub) {
        super(player);
        this.clientstub = clientstub;
    }

    public PlayerAdapter(int id, IClient clientstub) {
        super(id);
        this.clientstub = clientstub;
    }

    public IClient getClientstub() {
        return clientstub;
    }

    public void setClientstub(IClient clientstub) {
        this.clientstub = clientstub;
    }
    
}
