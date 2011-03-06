package vkm.client;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vkm.Player;
import vkm.server.IServer;
import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.MoveListener;
import at.falb.games.alcatraz.api.Prisoner;

public class Client extends UnicastRemoteObject 
	implements IClient, MoveListener, Serializable
{
	private int numberOfPlayers;
	private IServer sro;
	private Player player;
	private Player [] players;
	private List<IClient> clients = new ArrayList<IClient>();
	private String serverAddress;
	private int clientPort, serverPort;
	private Alcatraz alcatraz;

	public static void main(String[] args)
	{
		System.out.println("USAGE: Client [Name] [numberOfPlayers] [clientPort] [serverAddress] [serverPort]");
		try
		{
			Client client = new Client(args);
			client.startClientRegistry(client.clientPort);
			client.publishClientObject();
			
			IServer server = client.bindServerObject();				
			server.Register(client.player, client.numberOfPlayers);
			System.out.println(server.getRegisteredUsers());
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public Client(String[] args) throws java.rmi.RemoteException
	{
		try
		{
			player = new Player();
			player.Name = args.length > 3 ? args[0] : "Player";
			numberOfPlayers = args.length > 1 ? Integer.parseInt(args[1]) : 2;
			clientPort = args.length > 2 ? Integer.parseInt(args[2]) : 20010;
			serverAddress = args.length > 3 ? args[3] : "192.168.0.6";
			serverPort = args.length > 4 ? Integer.parseInt(args[4]) : 20000;			
			
			System.out.println(InetAddress.getLocalHost().getHostAddress());
			player.Adress ="rmi://"+InetAddress.getLocalHost().getHostAddress()+":"+(clientPort)+"/AlcatrazClient";
		} 
		catch (UnknownHostException e)
		{
			System.out.println("Cannot locate ip-address");
			e.printStackTrace();
		}
	}
	private void startClientRegistry(int clientPort) throws RemoteException
	{
		//Startet die Client Registry
		try
		{
			java.rmi.registry.LocateRegistry.createRegistry(clientPort);
			System.out.println("Registry startet at port "+clientPort);
		} 
		catch (RemoteException e)
		{	
			e.printStackTrace();
			throw new RemoteException("Error while starting registry");	
		}
	}
	private void publishClientObject() throws Exception
	{
		//Gibt Client Objekt auf die Client Registry damit Server zugriff hat
		//zur Info, wenn Spiel gestartet wird
	    try 
	    {
	    	System.out.println("Rebind client object at rmi-registry: " + player.Adress);
	    	Naming.rebind(player.Adress, this);
	    } 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    	throw new Exception("Error while publishing ClientObject");	
	    }
	}
	public IServer bindServerObject()
	{
		//Bindet Serverobjekt zum Aufrufen der Methoden
		try
		{
			String address = "rmi://"+serverAddress+":"+serverPort+"/AlcatrazService";
			System.out.println("Bind server object from registry: " + address);
			sro =(IServer)Naming.lookup(address);
		}
		catch (Exception e)
		{
			System.out.println("Error: ");
			e.printStackTrace();
		}
		return sro;
	}

	public void StartGame(Player[] players) 
	{
		this.players = players;
		// called either by server or by first player
		System.out.println("--------->Client starts now the game<------------");
		
		// bind other clients
		for (int i = 0; i < players.length; i++)
			if (PlayerIndex(players) != i)
				clients.add(bindClientObject(players[i].Adress, players));
		
		// if this is the first player, start other players
		if (PlayerIndex(players) == 0) {
			for(Iterator<IClient> i = clients.iterator(); i.hasNext();) {
				try {
					i.next().StartGame(players);				
				} catch (Exception e) {
					System.out.println("Error: ");
					e.printStackTrace();
				}
			}
			// start game and make first move
			StartAlcatraz();			
		}
	}
	private int PlayerIndex(Player[] players)
	{
		for (int i = 0; i < players.length; i++) {
			if (players[i].Adress.equals(this.player.Adress)) {
				return i;
			}
		}
		return -1;
	}
	private IClient bindClientObject(String address, Player[] clients)
	{
		//Bindet Serverobjekt zum Aufrufen der Methoden
		try
		{
			System.out.println("Bind client object from registry: " + address);
			IClient client = (IClient)Naming.lookup(address);
			return client;
		}
		catch (Exception e)
		{
			System.out.println("Error: ");
			e.printStackTrace();
		}
		return null;
	}
	public void StartAlcatraz()
	{
		System.out.println("Start Alcatraz");
		alcatraz = new Alcatraz();
		alcatraz.init(players.length,PlayerIndex(players));
		for (int i = 0; i < players.length; i++) 
		{
			System.out.println("Set Alcatraz Player Name " + i + ":" + players[i].Name);
			alcatraz.getPlayer(i).setName(players[i].Name);
		}
		alcatraz.showWindow();
		alcatraz.addMoveListener(this);
		alcatraz.showWindow();
	}

	public void doMove(int playerId, int prisonerId, int rowOrCol, int row, int col) {
		// if youre not the first player, start now
		if (alcatraz == null)
			StartAlcatraz();
		
		alcatraz.doMove(alcatraz.getPlayer(playerId), alcatraz.getPrisoner(prisonerId), rowOrCol, row, col);
	}

	public void doMove(at.falb.games.alcatraz.api.Player player, Prisoner prisoner, int rowOrCol, int row, int col) {
		// TODO Auto-generated method stub
        System.out.println("moving " + prisoner + " to " + (rowOrCol == Alcatraz.ROW ? "row" : "col") + " " + (rowOrCol == Alcatraz.ROW ? row : col));
        new Worker(clients, player.getId(), prisoner.getId(), rowOrCol, row, col).run();
	}
	public void gameWon(at.falb.games.alcatraz.api.Player arg0) {
		// TODO Auto-generated method stub		
	}
	public void undoMove() {
		throw new RuntimeException("undoMove not supported");		
	}
	class Worker implements Runnable
	{
		final private int delay = 500, retry = 10;
		private List<IClient> clients;
		private int playerId;
		private int prisonerId;
		private int rowOrCol;
		private int row;
		private int col;
		Worker(List<IClient> clients, int playerId, int prisonerId, int rowOrCol, int row, int col)
		{
			this.clients=clients;
			this.playerId=playerId;
			this.prisonerId=prisonerId;
			this.rowOrCol=rowOrCol;
			this.row=row;
			this.col=col;
		}
		public void run() {
			// connect each client
	        for(Iterator<IClient> i = clients.iterator(); i.hasNext();) {
	        	// retry if required
	        	for (int j = 0; j < retry; j++) {
					try {
						// sleep between retries
						if (j > 0) {
							Thread.sleep((long)(delay * Math.pow(2, j)));							
						}
						i.next().doMove(playerId, prisonerId, rowOrCol, row, col);
						break;
					} catch (Exception e) {
						System.out.println("Error, retry: " + j);
						// stop, if all retries failed
						if (j >= retry) {
							e.printStackTrace();
							System.exit(-1);
						}
					}					
				}
			}			
		}
	}	
}
