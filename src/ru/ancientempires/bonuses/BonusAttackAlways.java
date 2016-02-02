package ru.ancientempires.bonuses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
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
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit)
	{
		return bonusAttack;
	}
	
	@Override
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit)
	{
		return bonusDefence;
	}
	
	@Override
	public int getSign()
	{
		return (int) Math.signum(bonusAttack + bonusDefence);
	}
	
	@Override
	public void saveJson(JsonObject object)
	{
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
	}
	
	@Override
	public void loadJson(JsonObject object, Rules rules)
	{
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeInt(bonusAttack);
		output.writeInt(bonusDefence);
	}
	
	@Override
	public void load(DataInputStream input, Rules rules) throws IOException
	{
		bonusAttack = input.readInt();
		bonusDefence = input.readInt();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + bonusAttack;
		result = prime * result + bonusDefence;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BonusAttackAlways other = (BonusAttackAlways) obj;
		if (bonusAttack != other.bonusAttack)
			return false;
		if (bonusDefence != other.bonusDefence)
			return false;
		return true;
	}
	
}
