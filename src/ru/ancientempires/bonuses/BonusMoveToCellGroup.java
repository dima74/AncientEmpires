package ru.ancientempires.bonuses;

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
	public void saveJSON(JsonObject object)
	{
		object.addProperty("group", group.name);
		object.addProperty("bonus", bonus);
	}
	
	@Override
	public void loadJSON(JsonObject object, Rules rules)
	{
		group = rules.getCellGroup(object.get("group").getAsString());
		bonus = object.get("bonus").getAsInt();
	}
	
}
