package ru.ancientempires.tasks;

import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class TaskRemoveBonus extends Task
{
	
	public Unit		unit;
	public Bonus	bonus;
	
	public TaskRemoveBonus()
	{}
	
	public TaskRemoveBonus(Game game)
	{
		setGame(game);
	}
	
	public TaskRemoveBonus setUnit(Unit unit)
	{
		this.unit = unit;
		return this;
	}
	
	public TaskRemoveBonus setBonus(Bonus bonus)
	{
		this.bonus = bonus;
		return this;
	}
	
	@Override
	public void run()
	{
		unit.bonuses.remove(bonus);
	}
	
}
