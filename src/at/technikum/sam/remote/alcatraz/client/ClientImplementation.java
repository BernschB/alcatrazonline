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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * TODO: Comment
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
     * TODO: comment
     *
     * @param host
     * @param port
     * @throws RemoteException
     */
    public boolean reportNewMaster(String host, int port) throws RemoteException {
        if (!masterServerUrl.equals(host) || (masterServerPort != port)) {
            this.masterServerUrl = host;
            this.masterServerPort = port;
            try {
                this.lookupMaster();
            } catch (Exception ex) {
                //TODO: Error Handling
                ex.printStackTrace();
            }
        }
        return true;
    }

    /**
     * TODO: comment
     *
     * @return
     * @throws RemoteException
     */
    public boolean isAlive() throws RemoteException {
        
        return true;
    }

    /*
     * TODO: DEBUG - remote-method used only or debug purposes remove when finished
     * When needed replace whith calls to isAlive() before deploying
     */
    public boolean debugIsAlive(String callerName)
            throws RemoteException {
        Util.printDebug("Is alive called by ".concat(callerName));
        return true;
    }

    /**
     * TODO: comment
     *
     * @param players
     * @return
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
                ex.printStackTrace();
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
            throw new GameStartException();
        }
        
        int i = 0;
        
        for (PlayerAdapter player : players) {
            this.game.getPlayer(i).setName(player.getName());
            i++;
        }
        //TODO: Remove debug
        while (i > 0) {
            i--;
            Util.printDebug("Player with id ".concat(String.valueOf(i)).
                    concat(" has the name: ").
                    concat(this.game.getPlayer(i).getName()));
        }
        
        
        
        this.game.addMoveListener(this);
        this.game.start();
        
        this.listener.gameStarted(game);
        
        return true;
    }
    
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
    
    public void playerAbsent(PlayerAdapter player) throws RemoteException {
        this.listener.playerAbsent(player.getName());
    }
   
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    public IRegistryServer getMasterServer() {
        return this.masterServer;
    }
    
    public void installListener(ClientListener listener) {
        this.listener = listener;
    }

    public PlayerAdapter getMyPlayer() {
        return myPlayer;
    }

    public PlayerAdapter getNextPlayer() {
        return nextPlayer;
    }

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
    public void moveDone(Player player, Prisoner prsnr, int i, int i1, int i2) {
        
        Move move = new Move(player, prsnr, i, i1, i2);
        DistributeMoveThread distributeMove = new DistributeMoveThread(this, move);
        distributeMove.start();
        
    }
    
    public void gameWon(Player player) {
        listener.gameWon(player);
    }
    // </editor-fold>
}
