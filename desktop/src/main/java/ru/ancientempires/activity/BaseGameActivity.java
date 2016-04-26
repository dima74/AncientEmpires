package ru.ancientempires.activity;

import ru.ancientempires.model.Game;
import ru.ancientempires.swing.GameComponent;

public class BaseGameActivity
{
	
	public static BaseGameActivity	activity;
									
	public GameComponent			view;
	public Game						game;
									
	public GameComponent getView()
	{
		return view;
	}
	
}
