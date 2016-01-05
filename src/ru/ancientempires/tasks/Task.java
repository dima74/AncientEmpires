package ru.ancientempires.tasks;

import java.util.HashMap;
import java.util.Map;

import ru.ancientempires.tasks.handlers.TaskHandler;

public class Task
{
	
	public TaskType		type;
	public int			turnToRun;
	
	Map<String, Object>	properties	= new HashMap<String, Object>();
	
	public Task(TaskType type, int diffTurn)
	{
		this.type = type;
		this.turnToRun = TaskHandler.game.currentTurn + diffTurn;
	}
	
	public Task setProperty(String name, Object property)
	{
		this.properties.put(name, property);
		return this;
	}
	
	public Object getProperty(String property)
	{
		return this.properties.get(property);
	}
	
}
