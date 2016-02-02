package ru.ancientempires.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.framework.MyAssert;
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
		MyAssert.a(unit != null);
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
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		unit = game.numberedUnits.get(input.readInt());
		bonus = game.numberedBonuses.get(input.readInt());
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeInt(game.numberedUnits.add(unit));
		output.writeInt(game.numberedBonuses.add(bonus));
	}
	
}
