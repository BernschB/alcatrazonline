package vkm.server;

import java.rmi.*;

public class Server
{
	private int registryPort = 20000;
	
	public Server()
	{

	}
	
	//Veröffentlicht Serverobjekt auf Server Registry
	public void publishObject()
	{
	    try 
	    {
	    	System.out.println("Create new instance of ServerRMIObject");
	    	IServer sro = new ClientToServerImpl();
	    	String address = "rmi://localhost:"+registryPort+"/AlcatrazService";
	    	System.out.println("Rebind server object at rmi-registry: " + address);
	    	Naming.rebind(address, sro);
	    } 
	    catch (Exception e) 
	    {
	    	System.out.println("Trouble: " + e);
	    }
	}
	
	//Startet Server Registry
	public void startRegistry()
	{
		try
		{
			java.rmi.registry.LocateRegistry.createRegistry(registryPort);
			System.out.println("Registry startet at port "+registryPort);
		} 
		catch (RemoteException e)
		{
			
			System.out.println("Error at while starting registry: ");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
			Server s = new Server();
			s.startRegistry();
			s.publishObject();

	}
}
