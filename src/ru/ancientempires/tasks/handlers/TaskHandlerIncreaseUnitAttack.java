package ru.ancientempires.tasks.handlers;

import ru.ancientempires.model.Unit;
import ru.ancientempires.tasks.Task;
import ru.ancientempires.tasks.TaskType;

public class TaskHandlerIncreaseUnitAttack extends TaskHandler
{
	
	public TaskHandlerIncreaseUnitAttack(TaskType type)
	{
		super(type);
	}
	
	@Override
	public TaskHandler newInstance()
	{
		return new TaskHandlerIncreaseUnitAttack(this.type);
	}
	
	@Override
	public void run(Task task)
	{
		Unit unit = (Unit) task.getProperty("unit");
		int value = (int) task.getProperty("value");
		unit.attack += value;
	}
	
}
