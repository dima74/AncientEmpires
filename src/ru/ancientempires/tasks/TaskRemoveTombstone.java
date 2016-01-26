package ru.ancientempires.tasks;

import ru.ancientempires.model.Game;

public class TaskRemoveTombstone extends TaskFrom
{
	
	public TaskRemoveTombstone()
	{}
	
	public TaskRemoveTombstone(Game game)
	{
		setGame(game);
	}
	
	@Override
	public void run()
	{
		game.fieldUnitsDead[i][j] = null;
	}
	
}
