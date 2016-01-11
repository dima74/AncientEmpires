package ru.ancientempires.tasks;

public class TaskIncreaseUnitAttack extends TaskUnitWithValue
{
	
	@Override
	public void run()
	{
		unit.attack += value;
	}
	
}
