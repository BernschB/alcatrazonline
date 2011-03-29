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
package at.technikum.sam.remote.alcatraz.server;

import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

/* TODO: check if synchronizing Singletons is possible ???
        Answer: No, its at least not recommended */
/**
 * Helper class for managing player lists and number of players and synchronize
 * them over spread.
 */
public class Game implements Serializable, Constants {

    private Vector<PlayerAdapter> players;
    private static int sequencer = -1;
    /**
     * Constructor of GameRegistry
     */
    public Game() {
        /*
         * Init Vector with size of MAXPLAYERS,
         * so that no resizing has to be performed.
         */
        players = new Vector<PlayerAdapter>(MAXPLAYERS);
        sequencer = -1;
    }

    /**
     * Adds an Player to the list of players for a new game.
     *
     * @param player The player which should be added
     * @return true if player is added, false if player already was registered
     * @throws GameRegistryException if a maximum number of players is reached
     * @throws NameAlreadyInUseException if a players name equals another players name
     */
    public boolean addPlayer(PlayerAdapter player) 
            throws GameRegistryException, NameAlreadyInUseException, RemoteException {

        if(player == null) {
            throw new NullPointerException();
        }
        
        if(players.size() >= MAXPLAYERS) {
            throw new GameRegistryException("A maximum number of players of "
                    .concat(String.valueOf(MAXPLAYERS))
                    .concat(" is already reached!"));
        }

        for(PlayerAdapter temp : players) {
            if(temp.getName().equals(player.getName())) {
                throw new NameAlreadyInUseException();
            }
            if(temp.getClientstub().equals(player.getClientstub())) {
                /** If the Player is already registered, nothing happens, but
                 *  false is returned
                 */
                return false;
            }
        }
        
        return this.players.add(player);
    }

    /**
     * Removes a player from the players list
     *
     * @param player The player which should be removed
     * @return true if the list of players contained the specified player
     */
    public boolean removePlayer(PlayerAdapter player) {
        return players.remove(player);
    }


    /**
     * Returns the current number of registerd players
     * @return the current number of registerd players
     */
    public int getNumberOfPlayers() {
        return this.players.size();
    }

    /**
     * Returns the complete list of players as an iterable Vector
     *
     * @return the player list
     */
    public Vector<PlayerAdapter> getPlayers() {
        return this.players;
    }

    /**
     * Deletes all registered players, and resets the server state.
     *
     * This should be used, if the server finalized one game and starts over
     * for the next one.
     */
    public void Reset() {
        this.players.clear();
        sequencer = -1;
    }

    /**
     * Answers the question if a player is participating at this game
     *
     * @param player the player who should be looked up
     * @return true if yes, false if no
     */
    public boolean hasPlayer(PlayerAdapter player) {
        return this.players.contains(player);
    }

    /** TODO: Player ID Sequencing is not aware of aborted games and its 
     * players. ID Collissions possible after aborting (resetting) a game
     */
    /**
     * Helper function to sequence playerID's and reset the sequencer
     * if a game started
     *
     * @return A number which isn't used as player ID in the current Game.
     */
    public static int getNewPlayerId() {
        return sequencer++;
    }

    /**
     * TODO: comment
     * @throws GameStartException
     * @throws RemoteException
     */
    public void startGame() throws GameStartException, RemoteException {
        PlayerAdapter p = null;
        Iterator i = this.getPlayers().iterator();
        int newid = -1;

        while (i.hasNext()) {
            p = (PlayerAdapter) i.next();
            /* Rearrange Player Id's from 0 to 3 */
            newid++;
            p.getClientstub().setPlayerId(newid);
            /* strt Game on client implementation */
            p.getClientstub().startGame(this.getPlayers());
        }

        this.Reset();
    }
}
