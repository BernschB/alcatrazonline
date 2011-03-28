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

import at.falb.games.alcatraz.api.Player;
import at.technikum.sam.remote.alcatraz.client.ClientImplementation;
import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.IRegistryServer;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.Util;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import spread.AdvancedMessageListener;
import spread.MembershipInfo;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

/**
 *
 * TODO: Comment
 */
public class RegistryServerImplementation extends UnicastRemoteObject
        implements IRegistryServer, AdvancedMessageListener,
        Serializable, Constants {

    private static Game currentGame = null;

    /**
     * TODO: comment
     * @throws RemoteException
     */
    public RegistryServerImplementation() throws RemoteException {
        currentGame = new Game();
        currentGame.Reset();
    }

    // <editor-fold defaultstate="collapsed" desc="IRegistryServer Implementation">
    /**
     * Creates a new player object with an unique player ID and the
     * given name and associates it with the requesters stub
     *
     * @param name the new players nickname
     * @return a PlayerAdapter object containing the player and its stub or
     * NULL if no player could be created
     * @throws NameAlreadyInUseException if the given is already used
     * @throws RemoteException if the associated client stub cannot be reached
     */
    public PlayerAdapter createPlayer(String name)
            throws NameAlreadyInUseException, RemoteException {

        if (nameInUse(name)) {
            throw new NameAlreadyInUseException();
        }

        Player newPlayer = new Player(Game.getNewPlayerId());
        newPlayer.setName(name);

        ClientImplementation client = null;

        try {
            client = (ClientImplementation) Naming.lookup("rmi:/".concat(getClientHost()).concat(":1099/").concat(RMI_CLIENT_SERVICE));
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (client != null) {
            return new PlayerAdapter(newPlayer, client);
        } else {
            return null;
        }
    }

    /**
     * Adds a given player to the current game.
     *
     * @param player a PlayerAdapter object identifying a player and its client
     *
     * @throws GameRegistryException when something went wrong while registration TODO
     * @throws RemoteException when ??? uhm...yeah when actually ??? TODO
     */
    public void register(PlayerAdapter player)
            throws GameRegistryException, RemoteException {
        try {
            currentGame.addPlayer(player);
            if (currentGame.getNumberOfPlayers() == MAXPLAYERS) {
                currentGame.startGame();
            }
            this.synchronizeGame();
        } catch (NameAlreadyInUseException ex) {

            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);

            throw new GameRegistryException(
                    String.format(EX_MSG_GAME_REGISTRY_FAILED,
                    player.getName()));
        } catch (GameStartException ex) {

            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Removes a given player from the current game
     *
     * @param player a PlayerAdapter object identifying a player and its client
     * @throws GameRegistryException when ??? uhm...yeah when actually ??? TODO
     * @throws RemoteException when ??? uhm...yeah when actually ??? TODO
     */
    public void unregister(PlayerAdapter player)
            throws GameRegistryException, RemoteException {
        currentGame.removePlayer(player);
        this.synchronizeGame();
    }

    /**
     * TODO: comment
     * @param player
     * @throws GameStartException
     * @throws RemoteException
     */
    public void forceStart(PlayerAdapter player)
            throws GameStartException, RemoteException {
        if (currentGame.hasPlayer(player) && currentGame.getNumberOfPlayers() > 1) {
            currentGame.startGame();
            this.synchronizeGame();
        } else {
            throw new GameStartException();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AdvancedMessageListener Implementation">
    /**
     * TODO: comment
     * @param spreadMessage
     */
    public void regularMessageReceived(SpreadMessage spreadMessage) {
        Util.printDebug("Regular message received.");
        try {
            String input = (String) spreadMessage.getObject();
            Util.printDebug(input);
             /**
             * TODO: Implement Server state synchronisation here
             */
        } catch (SpreadException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * TODO: comment
     * @param spreadMessage
     */
    public void membershipMessageReceived(SpreadMessage spreadMessage) {

        /* comment */
        Util.printDebug("membershipMessageReceived");
        /**
         * TODO: Implement group join/leave here
         */
        /**
         * TODO: Implement master server failover here
         */
        MembershipInfo membershipInformation = spreadMessage.getMembershipInfo();

        SpreadGroup members[] = membershipInformation.getMembers();
        if (membershipInformation.isRegularMembership()) {
            if (membershipInformation.isCausedByJoin()) {
                Util.printDebug("New member joined.");
                Util.printDebug("List of existing members.");
                for (SpreadGroup g : members) {
                    Util.printDebug(g.toString());
                }
            } else if (membershipInformation.isCausedByLeave()
                    || membershipInformation.isCausedByDisconnect()
                    || membershipInformation.isCausedByNetwork()) {
                Util.printDebug("Member left group.");
                Util.printDebug("List of existing members.");
                for (SpreadGroup g : members) {
                    Util.printDebug(g.toString());
                }
            }
        }

        SpreadMessage msg = spreadMessage;
        try {
            System.out.println("*****************RECTHREAD Received Message************");
            if (msg.isRegular()) {
                System.out.print("Received a ");
                if (msg.isUnreliable()) {
                    System.out.print("UNRELIABLE");
                } else if (msg.isReliable()) {
                    System.out.print("RELIABLE");
                } else if (msg.isFifo()) {
                    System.out.print("FIFO");
                } else if (msg.isCausal()) {
                    System.out.print("CAUSAL");
                } else if (msg.isAgreed()) {
                    System.out.print("AGREED");
                } else if (msg.isSafe()) {
                    System.out.print("SAFE");
                }
                System.out.println(" message.");

                System.out.println("Sent by  " + msg.getSender() + ".");

                System.out.println("Type is " + msg.getType() + ".");

                if (msg.getEndianMismatch() == true) {
                    System.out.println("There is an endian mismatch.");
                } else {
                    System.out.println("There is no endian mismatch.");
                }

                SpreadGroup groups[] = msg.getGroups();
                System.out.println("To " + groups.length + " groups.");

                byte data[] = msg.getData();
                System.out.println("The data is " + data.length + " bytes.");

                System.out.println("The message is: " + new String(data));
            } else if (msg.isMembership()) {
                MembershipInfo info = msg.getMembershipInfo();
                printMembershipInfo(info);
            } else if (msg.isReject()) {
                // Received a Reject message
                System.out.print("Received a ");
                if (msg.isUnreliable()) {
                    System.out.print("UNRELIABLE");
                } else if (msg.isReliable()) {
                    System.out.print("RELIABLE");
                } else if (msg.isFifo()) {
                    System.out.print("FIFO");
                } else if (msg.isCausal()) {
                    System.out.print("CAUSAL");
                } else if (msg.isAgreed()) {
                    System.out.print("AGREED");
                } else if (msg.isSafe()) {
                    System.out.print("SAFE");
                }
                System.out.println(" REJECTED message.");

                System.out.println("Sent by  " + msg.getSender() + ".");

                System.out.println("Type is " + msg.getType() + ".");

                if (msg.getEndianMismatch() == true) {
                    System.out.println("There is an endian mismatch.");
                } else {
                    System.out.println("There is no endian mismatch.");
                }

                SpreadGroup groups[] = msg.getGroups();
                System.out.println("To " + groups.length + " groups.");

                byte data[] = msg.getData();
                System.out.println("The data is " + data.length + " bytes.");

                System.out.println("The message is: " + new String(data));
            } else {
                System.out.println("Message is of unknown type: " + msg.getServiceType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Looks up, if a players nickname is in use already
     * TODO: synchronization issues??? race conditions?
     *
     * @param name the nickname that should be checked
     * @return true if its in use, false if not
     */
    private static boolean nameInUse(String name) {
        Vector<PlayerAdapter> v = currentGame.getPlayers();
        PlayerAdapter pa = null;
        Iterator i = v.iterator();

        while (i.hasNext()) {
            pa = (PlayerAdapter) i.next();
            if (pa.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * TODO: comment
     * @param group
     */
    private void electMaster(SpreadGroup group[]) {
        /* Preisfrage: Wie komme ich an die ID dieses Servers? */
    }

    /**
     * TODO: comment
     * @param info
     */
    private void printMembershipInfo(MembershipInfo info) {
        SpreadGroup group = info.getGroup();
        if (info.isRegularMembership()) {
            SpreadGroup members[] = info.getMembers();
            MembershipInfo.VirtualSynchronySet virtual_synchrony_sets[] = info.getVirtualSynchronySets();
            MembershipInfo.VirtualSynchronySet my_virtual_synchrony_set = info.getMyVirtualSynchronySet();

            System.out.println("REGULAR membership for group " + group
                    + " with " + members.length + " members:");
            for (int i = 0; i < members.length; ++i) {
                System.out.println("\t\t" + members[i]);
            }
            System.out.println("Group ID is " + info.getGroupID());

            System.out.print("\tDue to ");
            if (info.isCausedByJoin()) {
                System.out.println("the JOIN of " + info.getJoined());
            } else if (info.isCausedByLeave()) {
                System.out.println("the LEAVE of " + info.getLeft());
            } else if (info.isCausedByDisconnect()) {
                System.out.println("the DISCONNECT of " + info.getDisconnected());
            } else if (info.isCausedByNetwork()) {
                System.out.println("NETWORK change");
                for (int i = 0; i < virtual_synchrony_sets.length; ++i) {
                    MembershipInfo.VirtualSynchronySet set = virtual_synchrony_sets[i];
                    SpreadGroup setMembers[] = set.getMembers();
                    System.out.print("\t\t");
                    if (set == my_virtual_synchrony_set) {
                        System.out.print("(LOCAL) ");
                    } else {
                        System.out.print("(OTHER) ");
                    }
                    System.out.println("Virtual Synchrony Set " + i + " has "
                            + set.getSize() + " members:");
                    for (int j = 0; j < set.getSize(); ++j) {
                        System.out.println("\t\t\t" + setMembers[j]);
                    }
                }
            }
        } else if (info.isTransition()) {
            System.out.println("TRANSITIONAL membership for group " + group);
        } else if (info.isSelfLeave()) {
            System.out.println("SELF-LEAVE message for group " + group);
        }
    }

    /**
     * Synchronizes the current Game state to all server implementations in
     * this Spread Multicast Group
     */
    private void synchronizeGame() {
        SpreadMessage spreadMessage = new SpreadMessage();
        spreadMessage.addGroup(SPREAD_SERVER_GROUP_NAME); // notwendig ?
        try {
            spreadMessage.setObject(currentGame);
            spreadMessage.setReliable();
            spreadMessage.setFifo();
            RegistryServer.getSpreadConnection().multicast(spreadMessage);
        } catch (SpreadException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold>
}
