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

public class BonusOnCellGroup extends Bonus
{
	
	public CellGroup	group;
	public int			bonusAttack;
	public int			bonusDefence;
	
	public BonusOnCellGroup()
	{}
	
	public BonusOnCellGroup(CellGroup group, int bonusAttack, int bonusDefence)
	{
		this.group = group;
		this.bonusAttack = bonusAttack;
		this.bonusDefence = bonusDefence;
	}
	
	@Override
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit)
	{
		return checkCell(cell) ? bonusAttack : 0;
	}
	
	@Override
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit)
	{
		return checkCell(cell) ? bonusDefence : 0;
	}
	
	private boolean checkCell(Cell cell)
	{
		return group.contains(cell.type);
	}
	
	@Override
	public void saveJson(JsonObject object)
	{
		object.addProperty("group", group.name);
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
	}
	
	@Override
	public void loadJson(JsonObject object, Rules rules)
	{
		group = rules.getCellGroup(object.get("group").getAsString());
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeUTF(group.name);
		output.writeInt(bonusAttack);
		output.writeInt(bonusDefence);
	}
	
	@Override
	public void load(DataInputStream input, Rules rules) throws IOException
	{
		group = rules.getCellGroup(input.readUTF());
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
		BonusOnCellGroup other = (BonusOnCellGroup) obj;
		if (bonusAttack != other.bonusAttack)
			return false;
		if (bonusDefence != other.bonusDefence)
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
