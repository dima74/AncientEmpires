package ru.ancientempires.view.inputs;

import ru.ancientempires.handler.IGameHandler;
import ru.ancientempires.view.draws.GameDrawMain;

public abstract class InputBase extends IGameHandler
{
	
	public static GameDrawMain	gameDraw;
	public InputMain			main;
	
	public abstract void beginTurn();
	
	public abstract void tap(int i, int j);
	
	public abstract void endTurn();
	
}
