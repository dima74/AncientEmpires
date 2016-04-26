package ru.ancientempires.bonuses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.rules.Rules;

public class BonusAttackForUnit extends Bonus
{
	
	public UnitType	targetType;
	public int		bonusAttack;
	public int		bonusDefence;
	
	public BonusAttackForUnit()
	{}
	
	public BonusAttackForUnit(UnitType targetType, int bonusAttack, int bonusDefence)
	{
		this.targetType = targetType;
		this.bonusAttack = bonusAttack;
		this.bonusDefence = bonusDefence;
	}
	
	@Override
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit)
	{
		return checkUnit(targetUnit) ? bonusAttack : 0;
	}
	
	@Override
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit)
	{
		return checkUnit(fromUnit) ? bonusDefence : 0;
	}
	
	public boolean checkUnit(Unit unit)
	{
		return unit != null && unit.type == targetType;
	}
	
	@Override
	public int getSign()
	{
		return (int) Math.signum(bonusAttack + bonusDefence);
	}
	
	@Override
	public void saveJson(JsonObject object)
	{
		object.addProperty("targetType", targetType.name);
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
	}
	
	@Override
	public void loadJson(JsonObject object, Rules rules)
	{
		targetType = rules.getUnitType(object.get("targetType").getAsString());
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeUTF(targetType.name);
		output.writeInt(bonusAttack);
		output.writeInt(bonusDefence);
	}
	
	@Override
	public void load(DataInputStream input, Rules rules) throws IOException
	{
		targetType = rules.getUnitType(input.readUTF());
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
		result = prime * result + (targetType == null ? 0 : targetType.hashCode());
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
		BonusAttackForUnit other = (BonusAttackForUnit) obj;
		if (bonusAttack != other.bonusAttack)
			return false;
		if (bonusDefence != other.bonusDefence)
			return false;
		if (targetType == null)
		{
			if (other.targetType != null)
				return false;
		}
		else if (!targetType.equals(other.targetType))
			return false;
		return true;
	}
	
}
