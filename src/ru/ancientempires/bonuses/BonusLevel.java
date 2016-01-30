package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public class BonusLevel extends Bonus
{
	
	public int	multiAttack;
	public int	multiDefence;
	
	public BonusLevel()
	{}
	
	public BonusLevel(int multiAttack, int multiDefence)
	{
		this.multiAttack = multiAttack;
		this.multiDefence = multiDefence;
	}
	
	@Override
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit)
	{
		return unit.level * multiAttack;
	}
	
	@Override
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit)
	{
		return unit.level * multiDefence;
	}
	
	@Override
	public void saveJSON(JsonObject object)
	{
		object.addProperty("multiAttack", multiAttack);
		object.addProperty("multiDefence", multiDefence);
	}
	
	@Override
	public void loadJSON(JsonObject object, Rules rules)
	{
		multiAttack = object.get("multiAttack").getAsInt();
		multiDefence = object.get("multiDefence").getAsInt();
	}
	
}
