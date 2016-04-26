package ru.ancientempires.handler;

import com.google.gson.annotations.Expose;

import ru.ancientempires.model.Game;

public class IGameHandler
{

	@Expose
	public Game game;
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
}
