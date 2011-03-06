package vkm.server;

import java.io.Serializable;

import vkm.Player;

public class ListEntry implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player client;
	private int numberOfPlayers;
	
	public ListEntry(Player client, int numberOfPlayers)
	{
		this.client = client;
		this.numberOfPlayers = numberOfPlayers;
	}

	public Player getClient()
	{
		return client;
	}

	public void setClient(Player client)
	{
		this.client = client;
	}

	public int getNumberOfPlayers()
	{
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers)
	{
		this.numberOfPlayers = numberOfPlayers;
	}
	
}
