package ru.ancientempires.server;

import ru.ancientempires.model.Game;

public interface IServer
{
	
	public Game startGame(String gameID) throws Exception;
	
	public void stopGame(boolean startNext) throws Exception;
	
}
