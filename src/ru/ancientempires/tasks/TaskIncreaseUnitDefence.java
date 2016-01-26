package ru.ancientempires.tasks;

import ru.ancientempires.model.Game;

public class TaskIncreaseUnitDefence extends TaskUnitWithValue
{
	
	public TaskIncreaseUnitDefence()
	{}
	
	public TaskIncreaseUnitDefence(Game game)
	{
		setGame(game);
	}
	
	@Override
	public void run()
	{
		unit.defence += value;
	}
	
}
