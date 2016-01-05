package ru.ancientempires.tasks.handlers;

import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.tasks.Task;
import ru.ancientempires.tasks.TaskType;

public class TaskHandlerRemoveTombstone extends TaskHandler
{
	
	public TaskHandlerRemoveTombstone(TaskType type)
	{
		super(type);
	}
	
	@Override
	public TaskHandler newInstance()
	{
		return new TaskHandlerRemoveTombstone(this.type);
	}
	
	@Override
	public void run(Task task)
	{
		int i = (int) task.getProperty("i");
		int j = (int) task.getProperty("j");
		
		GameHandler.fieldDeadUnits[i][j] = null;
	}
	
}
