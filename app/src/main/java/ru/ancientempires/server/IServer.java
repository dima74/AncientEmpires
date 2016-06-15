package ru.ancientempires.server;

import ru.ancientempires.model.Game;

public interface IServer
{
	
	Game startGame(String gameID) throws Exception;
	
	void stopGame() throws Exception;
	
}
