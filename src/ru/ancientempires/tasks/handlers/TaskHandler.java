package ru.ancientempires.tasks.handlers;

import java.util.ArrayList;

import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.tasks.Task;
import ru.ancientempires.tasks.TaskType;

public abstract class TaskHandler extends GameHandler implements ITaskHandler
{
	
	public static TaskHandler[]	taskHandlers	= new TaskHandler[TaskType.amount];
	
	public static void addNewTask(Task task)
	{
		ArrayList<Task> tasks = GameHandler.game.tasks.get(task.turnToRun);
		if (tasks == null)
			GameHandler.game.tasks.put(task.turnToRun, tasks = new ArrayList<Task>());
		tasks.add(task);
	}
	
	public static void runTasks()
	{
		ArrayList<Task> tasks = GameHandler.game.tasks.get(GameHandler.game.currentTurn);
		if (tasks != null)
		{
			for (Task task : GameHandler.game.tasks.get(GameHandler.game.currentTurn))
				TaskHandler.getTaskHandler(task).run(task);
			GameHandler.game.tasks.remove(GameHandler.game.currentTurn);
		}
	}
	
	public static TaskHandler getTaskHandler(Task task)
	{
		TaskHandler taskHandler = TaskHandler.taskHandlers[task.type.ordinal].newInstance();
		return taskHandler;
	}
	
	public static void init()
	{
		TaskHandler[] taskHandlers = new TaskHandler[]
		{
				new TaskHandlerRemoveTombstone(TaskType.TASK_REMOVE_TOMBSTONE),
				new TaskHandlerIncreaseUnitAttack(TaskType.TASK_INCREASE_UNIT_ATTACK),
				new TaskHandlerIncreaseUnitDecrease(TaskType.TASK_INCREASE_UNIT_DEFENSE)
		};
		for (TaskHandler taskHandler : taskHandlers)
			TaskHandler.taskHandlers[taskHandler.type.ordinal] = taskHandler;
	}
	
	public TaskType	type;
	
	public TaskHandler(TaskType type)
	{
		this.type = type;
	}
	
}
