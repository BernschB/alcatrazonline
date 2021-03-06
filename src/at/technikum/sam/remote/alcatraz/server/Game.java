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

import at.technikum.sam.remote.alcatraz.commons.ClientAlreadyRegisteredException;
import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for managing player lists and number of players and synchronize
 * them over spread.
 */
public class Game implements Serializable, Constants {

    private List<PlayerAdapter> players;
    private static int sequencer = -1;
    private String masterHost = "";
    private int masterPort = 0;

    /**
     * Constructor of GameRegistry
     */
    public Game() {
        this.players = Collections.synchronizedList(new ArrayList<PlayerAdapter>(MAXPLAYERS));

    }

    /**
     * Adds an Player to the list of players for a new game.
     *
     * @param player The player which should be added
     * @throws GameRegistryException if a maximum number of players is reached
     * @throws NameAlreadyInUseException if a players name equals another players name
     */
    public void addPlayer(PlayerAdapter player)
            throws GameRegistryException, NameAlreadyInUseException, ClientAlreadyRegisteredException, RemoteException {

        if (player == null) {
            throw new NullPointerException();
        }

        if (players.size() >= MAXPLAYERS) {
            throw new GameRegistryException("A maximum number of players of ".concat(String.valueOf(MAXPLAYERS)).concat(" is already reached!"));
        }

        for (PlayerAdapter temp : players) {
            if (temp.getName().equals(player.getName())) {
                throw new NameAlreadyInUseException();
            }
            if (temp.getClientstub().equals(player.getClientstub())) {

                throw new ClientAlreadyRegisteredException();
            }
        }
        this.players.add(player);
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
    public List<PlayerAdapter> getPlayers() {
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

    /**
     * Starts the game
     *
     * @throws GameStartException
     */
    public void startGame() throws GameStartException {
        
        if (this.getPlayers().size() < MINPLAYERS) {
            throw new GameStartException("Not enough player registered");
        }

        List<PlayerAdapter> playerList = this.getPlayers();

        for (PlayerAdapter player : this.getPlayers()) {
            try {
                player.getClientstub().startGame(playerList);
            } catch (RemoteException ex) {
               // leave the handling of absent players to the clients
                Logger.getLogger(Game.class.getName()).log(Level.INFO, null, ex);
            }
        }

        this.Reset();
    }

    /**
     * Returns a string representation of this class
     *
     * @return a readable string representing this class
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Current Game has ");
        sb.append(this.getNumberOfPlayers());
        sb.append(" players registered:\n");

        for (PlayerAdapter player : this.players) {
            sb.append("  ");
            sb.append(player.getName());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public int getMasterPort() {
        return masterPort;
    }
}
