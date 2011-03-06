package vkm.server;

import spread.*;

import java.net.*;
import java.util.ArrayList;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.*;
import vkm.Player;

public class ClientToServerImpl extends UnicastRemoteObject implements IServer, BasicMessageListener
{
	private static final long serialVersionUID = 1L;
	private ArrayList<ListEntry> waitingClients;
	private int actualNumberOfPlayers;
	private vkm.client.IClient stc;
	private SpreadConnection connection;
	private SpreadGroup group;
	private String daemonAddress = "localhost";
	private int daemonPort = 3333;
	private boolean spreadSender = false;
	
	public ClientToServerImpl() throws java.rmi.RemoteException
	{
		waitingClients = new ArrayList<ListEntry>();
		
		try
		{
			connection = new SpreadConnection();
			connection.connect(InetAddress.getByName(daemonAddress), daemonPort, "Server", false, true);
			connection.add(this);
			group = new SpreadGroup();
			group.join(connection, "alcatrazgroup");
		}
		catch(SpreadException e)
		{
			System.err.println("There was an error connecting to the daemon.");
			e.printStackTrace();
			System.exit(1);
		}
		catch(UnknownHostException e)
		{
			System.err.println("Can't find the daemon " + daemonAddress);
			System.exit(1);
		}	
		
		//Nur zum Testen
//		Player p = new Player();
//		p.Name="Manfred";
//		ListEntry le = new ListEntry(p,2);
//		spreadInformation(le);
//		System.out.println("FERTIG GESPREADED");
	}
	
	//Registriert neuen Client an Registry
	//Spreaded "Entry" an alle anderen Server
	public void Register(Player client, int numberOfPlayers)
	{
		actualNumberOfPlayers = numberOfPlayers;
		ListEntry entry = new ListEntry(client, numberOfPlayers);
		waitingClients.add(entry);
		System.out.println("New client has been registered");
		
		spreadInformation(entry);
		lookForMatchingUsers();
	}
	
	//Sieht nach ob es genügend Spieler für ein Spiel gibt
	private void lookForMatchingUsers()
	{
		int player=0;
		System.out.println("Looking for matching users:");
		
		for(ListEntry le:waitingClients)
		{
			Player p = le.getClient();
			System.out.println(p.Name+" "+le.getNumberOfPlayers());
			if(le.getNumberOfPlayers() == actualNumberOfPlayers)
				player++;
		}
		System.out.println();
		
		if(player == actualNumberOfPlayers)
		{
			informClientToStartGame();
		}
	}
	
	
	//Informiert ersten Client der dazukam zum Starten des Spiels
	//Und löscht dannach diese Spieler aus Liste
	private void informClientToStartGame()
	{
		Player[] clients = new Player[actualNumberOfPlayers];
		int i=0;
		for(ListEntry le:waitingClients)
		{
			if(le.getNumberOfPlayers() == actualNumberOfPlayers)
			{
				clients[i] = le.getClient();
				i++;
			}
		}

		stc = bindClientObject(clients[0].Adress);
	
		try
		{
			System.out.println("Inform client "+ clients[0].Name +" to start game!");
			stc.StartGame(clients);
			System.out.println("Client was informed to start game!");
			
			//Delete players from list
			ListEntry[] deletions = new ListEntry[this.actualNumberOfPlayers];
			int t=0;
			for(ListEntry le:waitingClients)
			{
				for(i=0; i<clients.length; i++)
				{
					if(le.getClient().Name == clients[i].Name &&
					   le.getClient().Adress == clients[i].Adress)
					{
						deletions[t]=le;
						t++;
					}
				}
			}
			
			for(i=0; i<deletions.length; i++)
			{
				waitingClients.remove(deletions[i]);
				System.out.println(((Player)deletions[i].getClient()).Name+ " deleted from list");
			}
			
			System.out.println(getRegisteredUsers());
		} 
		catch (RemoteException e)
		{
			System.out.println("Error while sending clients to starting player");
			e.printStackTrace();
		}
	}
	
	//Binded aktuellen Client Objekt, zum Informieren "Starte Spiel"
	private vkm.client.IClient bindClientObject(String clientAddress)
	{
		try
		{
			System.out.println("Bind client object from registry: " + clientAddress);
			stc =(vkm.client.IClient)Naming.lookup(clientAddress);
		}
		catch (Exception e)
		{
			System.out.println("Error: ");
			e.printStackTrace();
		}
		return stc;

	}
	
	//Liefert String zur Ausgabe der aktuellen registrierten Spieler
	public String getRegisteredUsers()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("---------------------------\n");
		buf.append("Currently registered users:\n");
		for(ListEntry le:waitingClients)
		{
			String name   = le.getClient().Name;
			String adress = le.getClient().Adress;
			int nOp       = le.getNumberOfPlayers();
			buf.append("Name:"+name+" - IP:"+adress+" - Players:"+nOp+"\n");
		}
		buf.append("--------End of list--------\n");
		
		return buf.toString();
	}

	/////////SPREAD ///////////////////////////////////////////////////
	
	//Sendet das ListEntry Objekt an alle Server
	private void spreadInformation(Object o)
	{
		spreadSender = true;
		SpreadMessage message = new SpreadMessage();
		message.addGroup("alcatrazgroup");
		message.setReliable();
		message.setFifo();
		ListEntry le = (ListEntry)o;
				
		try
		{
			message.setObject(le);
			connection.multicast(message);
		} 
		catch (SpreadException e)
		{
			e.printStackTrace();
		}
	}
	
	//Server erhalten das ListEntry Objekt und sollten dann die Register Methode aufrufen
	//Diese wird aber nicht aufgerufen, da ja sonst wieder spreadInformation gestartet wird
	//Stattdessen steht das alles hier nochmal drinnen
	//Der Sender der Spread Nachricht hat Attribut spreadSender=true, dieser führt das nicht mehr aus!
	//Attribut wird dann wieder auf false gesetzt
	public void messageReceived(SpreadMessage msg)
	{
		
		if(msg.isRegular())
		{
			
			try
			{
				ListEntry le = (ListEntry)msg.getObject();
				Player p = le.getClient();
				int nop = le.getNumberOfPlayers();			
					
				System.out.println("\n--------------------------------------------------------");
				System.out.println("IncomingSpreadMessage: Registriere "+p.Name+" fuer "+nop+ " Spieler");
				System.out.println("Absender: "+msg.getSender());
				
				//Register Methode, für Empfänger, da sonst endlosaufruf
				//Weil ja sonst immer spreadInformation aufgerufen wird!?
				if(spreadSender == false)
				{
					System.out.println("Spieler wird registriert, da Absender<>Empfaenger");
					actualNumberOfPlayers = nop;
					ListEntry entry = new ListEntry(p, nop);
					waitingClients.add(entry);
					System.out.println("New client has been registered");
					lookForMatchingUsers();
				}
				else
				{
					spreadSender = false;
					System.out.println("Spieler wird NICHT registriert, da Absender=Empfaenger");
				}

			} 
			catch (SpreadException e)
			{
				e.printStackTrace();
			}
			
			System.out.println("--------------------------------------------------------\n");
			
			
		}
		
	}

}


