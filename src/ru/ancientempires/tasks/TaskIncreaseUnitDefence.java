package ru.ancientempires.tasks;

public class TaskIncreaseUnitDefence extends TaskUnitWithValue
{
	
	@Override
	public void run()
	{
		unit.defence += value;
	}
	
}
