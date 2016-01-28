package ru.ancientempires.bonuses;

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
			BonusAttackAlways.class,
			BonusAttackForUnit.class,
			BonusMoveToCellGroup.class,
			BonusOnCellGroup.class);
			
	public int ordinal()
	{
		return Bonus.classes.indexOf(getClass());
	}
	
	public int getBonusAttack(Game game, Unit unit, Unit targetUnit)
	{
		return 0;
	}
	
	public int getBonusDefence(Game game, Unit unit, Unit fromUnit)
	{
		return 0;
	}
	
	public int getBonusMove(Game game, Unit unit, Cell targetCell)
	{
		return 0;
	}
	
	public int getBonusMoveStart(Game game, Unit unit)
	{
		return 0;
	}
	
	public int getSign()
	{
		return 0;
	}
	
	public void saveJSON(JsonObject object)
	{}
	
	public void loadJSON(JsonObject object, Rules rules)
	{}
	
}
