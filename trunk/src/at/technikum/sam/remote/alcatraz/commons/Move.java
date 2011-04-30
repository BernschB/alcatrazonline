/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.sam.remote.alcatraz.commons;

import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;

/**
 *
 * @author Sebastian_2
 */
public class Move {
    private Player player;
    private Prisoner prsnr;
    private int rowOrCol;
    private int row;
    private int col;
    
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
