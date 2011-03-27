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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import spread.AdvancedMessageListener;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;
import spread.MembershipInfo;

import at.technikum.sam.remote.alcatraz.commons.Util;

/**
 *
 * TODO: comment
 */
public class RegistryServer implements AdvancedMessageListener, Constants {

  private static String spreadHost = "localhost";
  private SpreadConnection connection;
  private SpreadGroup group;

  public static void main(String[] args) {
    new RegistryServer();
  }

  public RegistryServer() {
    try {
      System.out.println("Joining Spread Server Group...");
      try {
        /* Connect to local spread daemon and get spread group */
        connection = new SpreadConnection();
        connection.connect(InetAddress.getByName(spreadHost),
                0,
                SPREAD_SERVER_GROUP_NAME,
                true,
                true);

        /* Add AdvancedMessageListener Implementation to this Spread connection */
        connection.add(this);
      } catch (SpreadException e) {
        System.err.println("There was an error connecting to the daemon.");
        e.printStackTrace();
        System.exit(1);
      } catch (UnknownHostException e) {
        System.err.println("Can't find the spread daemon on" + spreadHost);
        System.exit(1);
      }

      System.out.println("Starting up Registry Server instance...");
      RegistryServerImplementation r = new RegistryServerImplementation();
      System.out.println("Creating Registry...");
      java.rmi.registry.LocateRegistry.createRegistry(1099);
      Naming.rebind("rmi://localhost:1099/".concat(RMI_SERVER_SERVICE), r);
      System.out.println("Registry Server up and running.");
    } catch (Exception e) {
      System.out.println("Something went wrong while bringing up server.");
      e.printStackTrace();
    }
    byte buffer[] = new byte[80];
    do {
      try {
        System.in.read(buffer);
        if (buffer[0] == 'j') {
          group = new SpreadGroup();
          group.join(connection, SPREAD_SERVER_GROUP_NAME);
        } else if (buffer[0] == 'l') {
          group.leave();
        }
      } catch (Exception e) {
        System.out.println("Something went wrong while bringing up server.");
        e.printStackTrace();
      }
    } while (buffer[0] != 'q');
  }

  public void regularMessageReceived(SpreadMessage sm) {
    throw new UnsupportedOperationException("Not supported yet.");

    /* Blabhdhfod */

    /**
     * TODO: Implement Server state synchronisation here
     */
  }

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

  private void electMaster(SpreadGroup group[]) {
    /* Preisfrage: Wie komme ich an die ID dieses Servers? */
  }

  	private void printMembershipInfo(MembershipInfo info)
	{
	        SpreadGroup group = info.getGroup();
		if(info.isRegularMembership()) {
			SpreadGroup members[] = info.getMembers();
			MembershipInfo.VirtualSynchronySet virtual_synchrony_sets[] = info.getVirtualSynchronySets();
			MembershipInfo.VirtualSynchronySet my_virtual_synchrony_set = info.getMyVirtualSynchronySet();

			System.out.println("REGULAR membership for group " + group +
					   " with " + members.length + " members:");
			for( int i = 0; i < members.length; ++i ) {
				System.out.println("\t\t" + members[i]);
			}
			System.out.println("Group ID is " + info.getGroupID());

			System.out.print("\tDue to ");
			if(info.isCausedByJoin()) {
				System.out.println("the JOIN of " + info.getJoined());
			}	else if(info.isCausedByLeave()) {
				System.out.println("the LEAVE of " + info.getLeft());
			}	else if(info.isCausedByDisconnect()) {
				System.out.println("the DISCONNECT of " + info.getDisconnected());
			} else if(info.isCausedByNetwork()) {
				System.out.println("NETWORK change");
				for( int i = 0 ; i < virtual_synchrony_sets.length ; ++i ) {
					MembershipInfo.VirtualSynchronySet set = virtual_synchrony_sets[i];
					SpreadGroup         setMembers[] = set.getMembers();
					System.out.print("\t\t");
					if( set == my_virtual_synchrony_set ) {
						System.out.print( "(LOCAL) " );
					} else {
						System.out.print( "(OTHER) " );
					}
					System.out.println( "Virtual Synchrony Set " + i + " has " +
							    set.getSize() + " members:");
					for( int j = 0; j < set.getSize(); ++j ) {
						System.out.println("\t\t\t" + setMembers[j]);
					}
				}
			}
		} else if(info.isTransition()) {
			System.out.println("TRANSITIONAL membership for group " + group);
		} else if(info.isSelfLeave()) {
			System.out.println("SELF-LEAVE message for group " + group);
		}
	}

}
