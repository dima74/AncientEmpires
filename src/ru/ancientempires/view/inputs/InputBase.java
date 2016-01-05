package ru.ancientempires.view.inputs;

import ru.ancientempires.model.Game;
import ru.ancientempires.view.draws.GameDrawMain;

public abstract class InputBase
{
	
	public static GameDrawMain	gameDraw;
	public static Game			game;
	
	public abstract void beginTurn();
	
	public abstract void tap(int i, int j);
	
	public abstract void endTurn();
	
}
