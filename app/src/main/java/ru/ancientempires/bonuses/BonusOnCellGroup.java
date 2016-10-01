package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusOnCellGroup extends Bonus
{

	public CellGroup group;
	public int       bonusAttack;
	public int       bonusDefence;

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

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("group", group.getNumber());
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
		return object;
	}

	public BonusOnCellGroup fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		group = CellGroup.newInstance(object.get("group").getAsInt(), info);
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
		return this;
	}

}
