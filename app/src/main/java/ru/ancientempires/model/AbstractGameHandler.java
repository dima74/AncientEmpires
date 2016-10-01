package ru.ancientempires.model;

import com.google.gson.annotations.Expose;

public class AbstractGameHandler
{

	@Expose
	public Game game;

	public void setGame(Game game)
	{
		this.game = game;
	}

}
