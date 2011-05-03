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
import at.falb.games.alcatraz.api.MoveListener;
import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.IClient;
import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.IRegistryServer;
import at.technikum.sam.remote.alcatraz.commons.Move;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import at.technikum.sam.remote.alcatraz.commons.Util;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ClientImplementation.java
 * contains the main part of the business logic on the client side
 * 
 */
public class ClientImplementation implements IClient, MoveListener {
    
    private String masterServerUrl;
    private int masterServerPort;
    private Alcatraz game;
    private PlayerAdapter myPlayer;
    private PlayerAdapter nextPlayer;
    private IRegistryServer masterServer;
    private List<PlayerAdapter> thePlayers = null;
    ;
    private ClientListener listener;
    
    public ClientImplementation() {
        super();
    }
    
    /**
     * has to be called before all other methods!
     * 
     * @param host hostname of the server
     * @param port portnumber of the server
     * @param myPlayer my PlayerAdapter
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException 
     */
    public void init(String host, int port, PlayerAdapter myPlayer)
            throws RemoteException, NotBoundException, MalformedURLException {
        this.masterServerUrl = host;
        this.masterServerPort = port;
        this.game = new Alcatraz();
        this.myPlayer = myPlayer;
        
        this.lookupMaster();
        
    }

    //<editor-fold defaultstate="collapsed" desc="IClient Implementation">
    /**
     * Implementation of the IClient method
     *
     * @param host hostname of the new server
     * @param port portnumber of the new server
     * @return true if reportNewMaster succeeds
     * @throws RemoteException
     */
    public boolean reportNewMaster(String host, int port) throws RemoteException {
        if (!masterServerUrl.equals(host) || (masterServerPort != port)) {
            this.masterServerUrl = host;
            this.masterServerPort = port;
            try {
                this.lookupMaster();
            } catch (Exception ex) {
               Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    /**
     * Implementation of the ICLient isAlive() method
     *
     * @return true if IClient is alive
     * @throws RemoteException
     */
    public boolean isAlive() throws RemoteException {
        
        return true;
    }

    /**
     * Implementation of the IClient debugIsAlive() method
     * 
     * @return true if IClient is alive
     * @param callerName name of the Caller
     * @throws RemoteException
     */
    public boolean debugIsAlive(String callerName)
            throws RemoteException {
        Util.printDebug("Is alive called by ".concat(callerName));
        return true;
    }

    /**
     * Implementation of the IClient startGame() method
     *
     * 
     * @param players list of all the players registered with the server
     * @return true if successfully finished
     * @throws GameStartException
     * @throws RemoteException
     */
    public boolean startGame(List<PlayerAdapter> players) throws GameStartException, RemoteException {
        this.thePlayers = players;
        
        boolean myClientFoundFlag = false;
        
        for (PlayerAdapter player : players) {
            try {
                player.getClientstub().debugIsAlive(this.myPlayer.getName());
            } catch (Exception ex) {
                Logger.getLogger(ClientImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new RemoteException();
            }
        }
        
        
        
        for (ListIterator<PlayerAdapter> it = players.listIterator(); it.hasNext();) {
            PlayerAdapter next = it.next();
            if (next.equals(this.myPlayer)) {
                
                this.game.init(players.size(), it.previousIndex());
                
                if (it.hasNext()) {
                    this.nextPlayer = it.next();
                } else {
                    this.nextPlayer = players.get(0);
                }
                myClientFoundFlag = true;
                break;
            }
            
        }
        Util.printDebug("Next Player is: ".concat(this.nextPlayer.getName()));
        
        
        if (!myClientFoundFlag) {
            throw new GameStartException("Player ".
                    concat(myPlayer.getName()).
                    concat(" says: myPlayer is not in the PlayerList"));
        }
        
        int i = 0;
        
        for (PlayerAdapter player : players) {
            this.game.getPlayer(i).setName(player.getName());
            i++;
        }
        if(Util.DEBUG) {
            while (i > 0) {
                i--;
                Util.printDebug("Player with id ".concat(String.valueOf(i)).
                        concat(" has the name: ").
                        concat(this.game.getPlayer(i).getName()));
            }
        }
            
        this.game.addMoveListener(this);
        this.game.start();
        
        this.listener.gameStarted(game);
        
        return true;
    }
    
    /**
     * Implementation of the IClient doMove() method
     * 
     * @param player the player which moved
     * @param prisoner the prisoner which was moved
     * @param rowOrCol 
     * @param row
     * @param col
     * @return true if successfully finished
     * @throws RemoteException 
     */
    public boolean doMove(Player player, Prisoner prisoner, int rowOrCol, int row, int col)
            throws RemoteException {
        this.game.doMove(
                this.game.getPlayer(player.getId()),
                this.game.getPrisoner(prisoner.getId()),
                rowOrCol,
                row,
                col);
        return true;
    }
    
    /**
     * Implementation of the IClient playerAbsent() method
     * 
     * @param player the player who is absent
     * @throws RemoteException 
     */
    public void playerAbsent(PlayerAdapter player) throws RemoteException {
        this.listener.playerAbsent(player.getName());
    }
   
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Returns the Server on which the client can perform the registration process
     * 
     * @return a IRegisterServer implementation
     */
    public IRegistryServer getMasterServer() {
        return this.masterServer;
    }
    
    /**
     * Method to install a Listener for certain Client events
     * 
     * @param listener the ClientListener implementation 
     */
    public void installListener(ClientListener listener) {
        this.listener = listener;
    }
    
    /**
     * returns the PlayerAdapter of my player
     * 
     * @return my PlayerAdapter 
     */

    public PlayerAdapter getMyPlayer() {
        return myPlayer;
    }
    
    /**
     * returns the PlayerAdapter of the next player
     * 
     * @return the next PlayerAdapter
     */

    public PlayerAdapter getNextPlayer() {
        return nextPlayer;
    }
    
    /**
     * returns a list of all players registered with this game
     * 
     * @return a list of PlayerAdapter 
     */

    public List<PlayerAdapter> getThePlayers() {
        return thePlayers;
    }
    
    

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Private Methods">
    private void lookupMaster()
            throws MalformedURLException, NotBoundException, RemoteException {
        
        try {
            this.masterServer = (IRegistryServer) Naming.lookup(
                    "rmi://".concat(this.masterServerUrl) // .concat(":")
                    //.concat(String.valueOf(this.masterServerPort))
                    .concat("/").concat(Constants.RMI_SERVER_SERVICE));
        } catch (MalformedURLException ex) {
            throw ex;
        } catch (NotBoundException ex) {
            throw ex;
        } catch (RemoteException ex) {
            throw ex;
        }
        
    }
    
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MoveListener Methods">
    /**
     * Implementation of the Alcatraz-Game MoveListener moveDone() method
     * 
     * @param player
     * @param prsnr
     * @param i
     * @param i1
     * @param i2 
     */
    public void moveDone(Player player, Prisoner prsnr, int i, int i1, int i2) {
        
        Move move = new Move(player, prsnr, i, i1, i2);
        DistributeMoveThread distributeMove = new DistributeMoveThread(this, move);
        distributeMove.start();
        
    }
    
    /**
     * Implementation of the Alcatraz-Game MoveListener moveDone() method
     * 
     * @param player 
     */
    public void gameWon(Player player) {
        listener.gameWon(player);
    }
    // </editor-fold>
}
