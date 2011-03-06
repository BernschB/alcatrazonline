package vkm.client;

import vkm.Player;

public interface IClient extends java.rmi.Remote  
{
	/**
	 * @param clients
	 * Called from server and sent to the first client only.
	 * Must succeed before the server can clear his state.
	 * The client is responsible distributing this.
	 */
	void StartGame(Player [] clients) throws java.rmi.RemoteException;
	/**
	 * @param moves
	 * Called whenever a client moves. Sent to all other clients.
	 */
	public void doMove(int playerId, int prisonerId, int rowOrCol, int row, int col) throws java.rmi.RemoteException;
	/**
	 * @param moves
	 * Called whenever next client should move. Sent to the next client.
	 */
	//void  NextMove();
}
