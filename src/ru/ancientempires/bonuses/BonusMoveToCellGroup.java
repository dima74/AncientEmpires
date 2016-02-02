package ru.ancientempires.bonuses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public class BonusMoveToCellGroup extends Bonus
{
	
	public CellGroup	group;
	public int			bonus;
	
	public BonusMoveToCellGroup()
	{}
	
	public BonusMoveToCellGroup(CellGroup group, int bonus)
	{
		this.group = group;
		this.bonus = bonus;
	}
	
	@Override
	public int getBonusMove(Game game, Unit unit, Cell cell, Cell targetCell)
	{
		return group.contains(targetCell.type) ? bonus : 0;
	}
	
	@Override
	public int getSign()
	{
		return (int) Math.signum(bonus);
	};
	
	@Override
	public void saveJson(JsonObject object)
	{
		object.addProperty("group", group.name);
		object.addProperty("bonus", bonus);
	}
	
	@Override
	public void loadJson(JsonObject object, Rules rules)
	{
		group = rules.getCellGroup(object.get("group").getAsString());
		bonus = object.get("bonus").getAsInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeUTF(group.name);
		output.writeInt(bonus);
	}
	
	@Override
	public void load(DataInputStream input, Rules rules) throws IOException
	{
		group = rules.getCellGroup(input.readUTF());
		bonus = input.readInt();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + bonus;
		result = prime * result + (group == null ? 0 : group.hashCode());
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
		BonusMoveToCellGroup other = (BonusMoveToCellGroup) obj;
		if (bonus != other.bonus)
			return false;
		if (group == null)
		{
			if (other.group != null)
				return false;
		}
		else if (!group.equals(other.group))
			return false;
		return true;
	}
	
}
