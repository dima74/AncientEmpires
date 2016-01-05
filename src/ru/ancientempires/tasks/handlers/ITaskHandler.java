package ru.ancientempires.tasks.handlers;

import ru.ancientempires.tasks.Task;

public interface ITaskHandler
{
	
	public void run(Task task);
	
	public TaskHandler newInstance();
	
}
