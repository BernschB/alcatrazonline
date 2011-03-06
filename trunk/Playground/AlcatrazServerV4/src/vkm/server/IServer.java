package vkm.server;

import vkm.Player;

/**
 * @author Harald
 *
 */
public interface IServer extends java.rmi.Remote 
{
	 
	
	public void Register(Player client, int numberOfPlayers) throws java.rmi.RemoteException;

	
	public String getRegisteredUsers() throws java.rmi.RemoteException;

}

