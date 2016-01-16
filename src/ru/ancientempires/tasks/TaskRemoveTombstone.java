package ru.ancientempires.tasks;

public class TaskRemoveTombstone extends TaskFrom
{
	
	@Override
	public void run()
	{
		game.fieldUnitsDead[i][j] = null;
	}
	
}
