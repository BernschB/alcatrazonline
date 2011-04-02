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
import at.technikum.sam.remote.alcatraz.commons.Util;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
    private List<PlayerAdapter> thePlayers = null;;
    private GameStartedListener listener;


    public ClientImplementation () {
        super();
    }

    public void init (String host, int port, PlayerAdapter myPlayer)
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
     public void debugIsAlive(String callerName)
            throws RemoteException {
         Util.printDebug("Is alive called by ".concat(callerName));
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

      for(PlayerAdapter player : players) {
          try {
            player.getClientstub().debugIsAlive(this.myPlayer.getName());
          } catch (Exception ex) {
              ex.printStackTrace();
              throw new RemoteException();
          }    
      }


        
       for(ListIterator<PlayerAdapter> it = players.listIterator(); it.hasNext(); ) {
            PlayerAdapter next = it.next();
            if(next.getName().equals(this.myPlayer.getName())) {
                
                this.game.init(players.size(), it.previousIndex());

                if(it.hasNext()){
                    this.nextPlayer = it.next();
                } else {
                    this.nextPlayer = players.get(0);
                }
                myClientFoundFlag = true;
                break;
           }

        }

        if(!myClientFoundFlag) {
            throw new GameStartException();
        }

        for(ListIterator<PlayerAdapter> it = players.listIterator(); it.hasNext(); ) {
            this.game.getPlayer(it.nextIndex()).setName(it.next().getName());
        }

        this.game.addMoveListener(this);
        this.game.start();

        this.listener.gameStarted(game);

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

    
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="Public Methods">

    public IRegistryServer getMasterServer () {
        return this.masterServer;
    }

    public void installListener (GameStartedListener listener) {
        this.listener = listener;
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
