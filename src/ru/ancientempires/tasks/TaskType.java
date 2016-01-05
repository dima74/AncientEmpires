package ru.ancientempires.tasks;

public class TaskType
{
	
	public static final TaskType	TASK_REMOVE_TOMBSTONE		= new TaskType();
	public static final TaskType	TASK_INCREASE_UNIT_ATTACK	= new TaskType();
	public static final TaskType	TASK_INCREASE_UNIT_DEFENSE	= new TaskType();
	
	public static TaskType[]		types						= new TaskType[]
																{
			TaskType.TASK_REMOVE_TOMBSTONE,
			TaskType.TASK_INCREASE_UNIT_ATTACK,
			TaskType.TASK_INCREASE_UNIT_DEFENSE
																};
	
	public static int				amount;
	
	public static void init()
	{
		TaskType.amount = TaskType.types.length;
		for (int i = 0; i < TaskType.amount; i++)
			TaskType.types[i].ordinal = i;
	}
	
	public int	ordinal;
	
}
