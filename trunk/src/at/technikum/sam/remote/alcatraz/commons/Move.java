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
import at.falb.games.alcatraz.api.Prisoner;

/**
 * Move.java
 * class to wrap the Alcatraz move in one structure
 */
public class Move {
    private Player player;
    private Prisoner prsnr;
    private int rowOrCol;
    private int row;
    private int col;
    
    /**
     * creates a new Move object
     * 
     * @param player 
     * @param prsnr
     * @param rowOrCol
     * @param row
     * @param col 
     */
    public Move(Player player, Prisoner prsnr, int rowOrCol, int row, int col) {
        this.player = player;
        this.prsnr = prsnr;
        this.rowOrCol = rowOrCol;
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public Player getPlayer() {
        return player;
    }

    public Prisoner getPrsnr() {
        return prsnr;
    }

    public int getRow() {
        return row;
    }

    public int getRowOrCol() {
        return rowOrCol;
    }
    
    
    
}
