package ru.ancientempires.server;

public interface IServer
{
	
	void startGame(String gameID) throws Exception;
	
	public void stopGame() throws Exception;
	
}
