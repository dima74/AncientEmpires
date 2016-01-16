package ru.ancientempires.handler;

import ru.ancientempires.client.Client;
import ru.ancientempires.model.Game;

public class IGameHandler
{
	
	public Game game = Client.getGame();
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
}
