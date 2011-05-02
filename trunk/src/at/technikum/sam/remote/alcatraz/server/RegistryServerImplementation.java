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

import at.technikum.sam.remote.alcatraz.commons.GameRegistryException;
import at.technikum.sam.remote.alcatraz.commons.GameStartException;
import at.technikum.sam.remote.alcatraz.commons.IRegistryServer;
import at.technikum.sam.remote.alcatraz.commons.NameAlreadyInUseException;
import at.technikum.sam.remote.alcatraz.commons.ClientAlreadyRegisteredException;
import at.technikum.sam.remote.alcatraz.commons.PlayerAdapter;
import at.technikum.sam.remote.alcatraz.commons.Constants;
import at.technikum.sam.remote.alcatraz.commons.IClient;
import at.technikum.sam.remote.alcatraz.commons.NotMasterException;
import at.technikum.sam.remote.alcatraz.commons.Util;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    private static boolean master = false;    // am I master?
    private static boolean synced = false;    // I received a sync message?

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
     * Adds a given player to the current game.
     *
     * @param player a PlayerAdapter object identifying a player and its client
     *
     * @throws NameAlreadyInUseException if the given is already used
     * @throws ClientAlreadyRegisteredException if the given client-stub is already registered
     * @throws GameRegistryException when something went wrong while registration TODO
     * @throws RemoteException when ??? uhm...yeah when actually ??? TODO
     */
    public void register(PlayerAdapter player)
            throws NameAlreadyInUseException, ClientAlreadyRegisteredException, GameRegistryException, RemoteException, NotMasterException {
        if (!master) {
            throw new  NotMasterException(currentGame.getMasterHost(),currentGame.getMasterPort());
        }
        try {
            player.getClientstub().debugIsAlive("Server register()");
        } catch (RemoteException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        Util.printDebug("Player ".concat(player.getName()).concat(" is alive"));

        try {
            currentGame.addPlayer(player);
            if (currentGame.getNumberOfPlayers() == MAXPLAYERS) {
                currentGame.startGame();
            }
            this.synchronizeGame();
        } catch (NameAlreadyInUseException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (ClientAlreadyRegisteredException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
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
        if (currentGame.getNumberOfPlayers() > 1) {
            currentGame.startGame();
            this.synchronizeGame();
        } else {
            throw new GameStartException();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="AdvancedMessageListener Implementation">
    /**
     * New regular message is received
     *
     * @param spreadMessage the received message
     */
    public void regularMessageReceived(SpreadMessage spreadMessage) {
        Util.printDebug("Regular message received.");
        try {
            Object o = spreadMessage.getObject();
            String input = "";
            if (o instanceof Game) {
                currentGame = (Game) o;   // save reveived object
                if (synced == false && master == true) {
                    reportMeAsNewMaster();  // if this server is the new master report this to the clients
                }
                synced = true;       // this must be set at a master to send messages
                input = currentGame.toString();
            } else if (o instanceof String) {
                input = (String) o;
            } else {
                input = o.toString();
            }

            Util.printDebug(input);
            Util.printDebug("Sync from ".concat(spreadMessage.getSender().toString()));
        } catch (SpreadException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * New membership message is received
     *
     * @param spreadMessage the received message
     */
    public void membershipMessageReceived(SpreadMessage spreadMessage) {
        Util.printDebug("membershipMessageReceived");
        MembershipInfo membershipInformation = spreadMessage.getMembershipInfo();

        SpreadGroup members[] = membershipInformation.getMembers();
        if (membershipInformation.isRegularMembership()) {
            // member joined group
            if (membershipInformation.isCausedByJoin()) {
                Util.printDebug("New member joined.   ".concat(membershipInformation.getJoined().toString()));
                Util.printDebug("List of existing members.");
                for (SpreadGroup g : members) {
                    Util.printDebug(g.toString());
                }
                this.electMaster(membershipInformation);  // elect new master
            // member left group because of one of the following reasons
            } else if (membershipInformation.isCausedByLeave()
                    || membershipInformation.isCausedByDisconnect()
                    || membershipInformation.isCausedByNetwork()) {
                //Util.printDebug("Member left group.   ".concat(membershipInformation.getLeft().toString()));
                Util.printDebug("List of existing members.");
                for (SpreadGroup g : members) {
                    Util.printDebug(g.toString());
                }
                this.electMaster(membershipInformation);  // elect new master
            }
        // this member left group
        } else if (membershipInformation.isSelfLeave()) {
            master = false;   // this server is not master anymore and must
            synced = false;   // be synced next time it connects to spread
            Util.printDebug("I'm out now.");
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Election for new master
     *
     * @param membershipInformation the membership information of the server
     */
    private void electMaster(MembershipInfo membershipInformation) {
        SpreadGroup group[] = membershipInformation.getMembers();
        if (group.length == 1) {   // only 1 entry => this server is new master
            if (membershipInformation.getJoined() != null) {
                Util.printDebug(CONF_PRIVATESPREADGROUP.concat(": I'm the first server online"));
            } else {
                Util.printDebug(CONF_PRIVATESPREADGROUP.concat(": I'm the last server online"));
            }
            master = true;
            synced = true;
        } else {  // more than 1 server online
            if (group[0].toString().contains(CONF_PRIVATESPREADGROUP)) { // this server is new master
                if (master) {        // server is current master?
                    this.synchronizeGame();
                    synced = true;
                    Util.printDebug(CONF_PRIVATESPREADGROUP.concat(": I'm the old and new master"));
                } else {                // no, wait for sync message from old master
                    Util.printDebug(CONF_PRIVATESPREADGROUP.concat(": I'm the new master"));
                    if (synced) {
                        this.synchronizeGame();
                        reportMeAsNewMaster();
                    }

                    master = true;
                    if (membershipInformation.getJoined() != null) {
                        if (membershipInformation.getJoined().toString().contains(CONF_PRIVATESPREADGROUP)) {
                            Util.printDebug(CONF_PRIVATESPREADGROUP.concat(": I'm new and I'm master... HELP"));
                        }
                    }
                }
            } else {      // do not become master
                if (master) {    // server is current master?
                    Util.printDebug(CONF_PRIVATESPREADGROUP.concat(": I was the old master"));
                    this.synchronizeGame();   // send sync to for master
                    master = false;
                    synced = false;
                } else {
                    Util.printDebug(CONF_PRIVATESPREADGROUP.concat(": I'm a slave"));
                    master = false;
                    synced = false;
                }
            }
        }
    }

    /**
     * Prints useful information about the received message
     * Only used for debugging
     *
     * @param info membership information
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
            currentGame.setMasterHost(CONF_REGISTRYSERVERHOSTNAME); // add hostname and port so a slave can tell a client where the master can be found
            currentGame.setMasterPort(Integer.parseInt(CONF_REGISTRYSERVERPORT));
            spreadMessage.setObject(currentGame);
            spreadMessage.setReliable();
            spreadMessage.setFifo();
            RegistryServer.getSpreadConnection().multicast(spreadMessage);
        } catch (SpreadException ex) {
            Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends the address and port of the new master to all clients.
     */
    private void reportMeAsNewMaster() {
        for (PlayerAdapter player : currentGame.getPlayers()) {
            IClient clientstub = player.getClientstub();
            try {
                clientstub.reportNewMaster(
                        CONF_REGISTRYSERVERHOSTNAME,
                        Integer.parseInt(CONF_REGISTRYSERVERPORT));
            } catch (RemoteException ex) {
                Logger.getLogger(RegistryServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // </editor-fold>
}
