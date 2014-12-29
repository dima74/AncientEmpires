package ru.ancientempires.view.algortihms;

import ru.ancientempires.client.Client;
import ru.ancientempires.model.Game;

public abstract class InputAlgorithm
{
	
	public InputAlgoritmMain	main;
	public static final Game	game	= Client.getClient().getGame();
	
	public InputAlgorithm(InputAlgoritmMain main)
	{
		this.main = main;
	}
	
	public void start(int i, int j)
	{}
	
	public boolean tap(int i, int j)
	{
		return false;
	}
	
	public void destroy()
	{
		// this.main.inputAlgorithms.remove(this);
	}
	
	public void end()
	{
		destroy();
	}
	
}
