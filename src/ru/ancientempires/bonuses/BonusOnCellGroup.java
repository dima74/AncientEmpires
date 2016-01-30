package ru.ancientempires.bonuses;

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
	public void saveJSON(JsonObject object)
	{
		object.addProperty("group", group.name);
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
	}
	
	@Override
	public void loadJSON(JsonObject object, Rules rules)
	{
		group = rules.getCellGroup(object.get("group").getAsString());
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
	}
	
}
