package ru.ancientempires.tasks;

import ru.ancientempires.model.Game;

public class TaskIncreaseUnitAttack extends TaskUnitWithValue
{
	
	public TaskIncreaseUnitAttack()
	{}
	
	public TaskIncreaseUnitAttack(Game game)
	{
		setGame(game);
	}
	
	@Override
	public void run()
	{
		unit.attack += value;
	}
	
}
