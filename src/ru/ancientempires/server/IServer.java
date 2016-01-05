package ru.ancientempires.server;

import java.io.IOException;

public interface IServer
{
	
	void startGame(String gameID) throws IOException;
	
	public void stopGame();
	
}
