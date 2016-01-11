package ru.ancientempires.tasks;

import ru.ancientempires.action.handlers.GameHandler;

public class TaskRemoveTombstone extends TaskFrom
{
	
	@Override
	public void run()
	{
		GameHandler.fieldDeadUnits[i][j] = null;
	}
	
}
