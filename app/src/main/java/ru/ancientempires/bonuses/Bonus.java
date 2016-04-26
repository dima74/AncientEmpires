package ru.ancientempires.bonuses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public abstract class Bonus
{
	
	public static List<Class<? extends Bonus>> classes = Arrays.asList(
			BonusLevel.class,
			BonusAttackAlways.class,
			BonusAttackForUnit.class,
			BonusMoveToCellGroup.class,
			BonusOnCellGroup.class,
			BonusCost.class);
			
	public static Bonus loadJsonBase(JsonObject object, Rules rules) throws Exception
	{
		int ordinal = object.get("type").getAsInt();
		Bonus bonus = Bonus.classes.get(ordinal).newInstance();
		bonus.loadJson(object, rules);
		return bonus;
	}
	
	public static Bonus loadBase(DataInputStream input, Rules rules) throws Exception
	{
		int ordinal = input.readInt();
		Bonus bonus = Bonus.classes.get(ordinal).newInstance();
		bonus.load(input, rules);
		return bonus;
	}
	
	public int ordinal()
	{
		return Bonus.classes.indexOf(getClass());
	}
	
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit)
	{
		return 0;
	}
	
	public final int getBonusAttack(Game game, Unit unit, Unit targetUnit)
	{
		return getBonusAttack(game, unit, unit.getCell(), targetUnit);
	}
	
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit)
	{
		return 0;
	}
	
	public final int getBonusDefence(Game game, Unit unit, Unit fromUnit)
	{
		return getBonusDefence(game, unit, unit.getCell(), fromUnit);
	}
	
	public int getBonusMove(Game game, Unit unit, Cell cell, Cell targetCell)
	{
		return 0;
	}
	
	public int getBonusMoveStart(Game game, Unit unit)
	{
		return 0;
	}
	
	public int getBonusCost(Game game, Unit unit)
	{
		return 0;
	}
	
	public int getSign()
	{
		return 0;
	}
	
	public final JsonObject saveJsonBase()
	{
		JsonObject result = new JsonObject();
		result.addProperty("type", ordinal());
		saveJson(result);
		return result;
	}
	
	public abstract void saveJson(JsonObject object);
	
	public abstract void loadJson(JsonObject object, Rules rules);
	
	public final void saveBase(DataOutputStream output) throws IOException
	{
		output.writeInt(ordinal());
		save(output);
	}
	
	public abstract void save(DataOutputStream output) throws IOException;
	
	public abstract void load(DataInputStream input, Rules rules) throws IOException;
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ordinal();
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bonus other = (Bonus) obj;
		if (ordinal() != other.ordinal())
			return false;
		return true;
	}
	
}
