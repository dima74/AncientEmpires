package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public class BonusAttackAlways extends Bonus
{
	
	public int	bonusAttack;
	public int	bonusDefence;
	
	public BonusAttackAlways()
	{}
	
	public BonusAttackAlways(int bonusAttack, int bonusDefence)
	{
		this.bonusAttack = bonusAttack;
		this.bonusDefence = bonusDefence;
	}
	
	@Override
	public int getBonusAttack(Game game, Unit unit, Unit targetUnit)
	{
		return bonusAttack;
	}
	
	@Override
	public int getBonusDefence(Game game, Unit unit, Unit fromUnit)
	{
		return bonusDefence;
	}
	
	@Override
	public int getSign()
	{
		return (int) Math.signum(bonusAttack + bonusDefence);
	}
	
	@Override
	public void saveJSON(JsonObject object)
	{
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
	}
	
	@Override
	public void loadJSON(JsonObject object, Rules rules)
	{
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
	}
	
}
