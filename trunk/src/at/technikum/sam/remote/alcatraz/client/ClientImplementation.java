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
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * TODO: Comment
 */
public class ClientImplementation extends UnicastRemoteObject implements IClient, Serializable, MoveListener {

    private String masterServerUrl;
    private int masterServerPort;
    private Alcatraz game;
    private Player player;
    private PlayerAdapter myPlayer;
    private PlayerAdapter nextPlayer;
    private IRegistryServer masterServer;
    private List<PlayerAdapter> thePlayers = null;;


    public ClientImplementation (String host, int port)
            throws RemoteException, NotBoundException, MalformedURLException {
        super();
        this.masterServerUrl = host;
        this.masterServerPort = port;
        this.game = new Alcatraz();
        this.player = new Player(0);

        
        try {
            this.lookupMaster();
        } catch (MalformedURLException ex) {
            throw ex;
        } catch (NotBoundException ex) {
            throw ex;
        } catch (RemoteException ex) {
            throw ex;
        }

    }



    //<editor-fold defaultstate="collapsed" desc="IClient Implementation">
    public void reportNewMaster(String host, int port) throws RemoteException {
       if(!masterServerUrl.equals(host) || (masterServerPort != port)) {
           this.masterServerUrl = host;
           this.masterServerPort = port;
           try {
               this.lookupMaster();
           } catch (Exception ex) {
               //TODO: Error Handling
               ex.printStackTrace();
           }
       }
    }

    public boolean isAlive() throws RemoteException {
       return true;
    }

    public boolean startGame(List<PlayerAdapter> players) throws GameStartException, RemoteException {
        this.thePlayers = players;

        ListIterator<PlayerAdapter> playerIter = players.listIterator();
        while(playerIter.hasNext()) {
            PlayerAdapter next = playerIter.next();
            if(next.getClientstub().equals(this)) {
                this.myPlayer = next;
                if(playerIter.hasNext()){
                    this.nextPlayer = playerIter.next();
                    break;
                } else {
                    this.nextPlayer = players.get(0);
                }
            }
        }

        this.game.init(players.size(), myPlayer.getId());

        for(PlayerAdapter player : players) {
            this.game.getPlayer(player.getId()).setName(player.getName());
        }

        this.game.addMoveListener(this);

        this.game.start();
        this.game.showWindow();

        return true;
    }

    public void doMove(Player player, Prisoner prisoner, int rowOrCol, int row, int col)
            throws RemoteException {
        this.game.doMove(player, prisoner, rowOrCol, row, col);
    }


    public void yourTurn() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void playerAbsent(PlayerAdapter player) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPlayerId() throws RemoteException {

    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="Public Methods">

    public IRegistryServer getMasterServer () {
        return this.masterServer;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Methods">

    private void lookupMaster() 
            throws MalformedURLException, NotBoundException, RemoteException {

        try {
            this.masterServer = (IRegistryServer) Naming.lookup(
                    "rmi://"
                    .concat(this.masterServerUrl)
                   // .concat(":")
                    //.concat(String.valueOf(this.masterServerPort))
                    .concat("/")
                    .concat(Constants.RMI_SERVER_SERVICE));
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
        for(PlayerAdapter currentPlayer : this.thePlayers){
            if(!(currentPlayer.equals(this.myPlayer) || currentPlayer.equals(this.nextPlayer))) {
                while (true) {
                    try {
                        currentPlayer.getClientstub().doMove(player, prsnr, i2, i2, i2);
                    } catch (RemoteException ex) {
                        // TODO: Error Handling => playerAbsent
                        try {
                            
                           Thread.sleep(Constants.TIMEOUT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    break;
                }
            }
        }

        while (true) {
            try {
                this.nextPlayer.getClientstub().doMove(player, prsnr, i2, i2, i2);
            } catch (RemoteException ex) {
                // TODO: Error Handling => playerAbsent
                try {

                   Thread.sleep(Constants.TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            break;
      }

    }

    public void gameWon(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     // </editor-fold>


}
