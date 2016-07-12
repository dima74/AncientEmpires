package ru.ancientempires.tasks;

import com.google.gson.JsonObject;

import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.WithNumbered;

public class TaskRemoveBonus extends Task
{

	@WithNumbered("numberedUnits")
	public Unit  unit;
	@WithNumbered("numberedBonuses")
	public Bonus bonus;
	
	public TaskRemoveBonus()
	{}
	
	public TaskRemoveBonus(Game game)
	{
		setGame(game);
	}
	
	public TaskRemoveBonus setUnit(Unit unit)
	{
		MyAssert.a(unit != null);
		this.unit = unit;
		return this;
	}
	
	public TaskRemoveBonus setBonus(Bonus bonus)
	{
		this.bonus = bonus;
		return this;
	}
	
	@Override
	public void run()
	{
		if (unit != null)
			MyAssert.a(unit.bonuses.remove(bonus));
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("unit", game.numberedUnits.add(unit));
		object.addProperty("bonus", game.numberedBonuses.add(bonus));
		return object;
	}

	public TaskRemoveBonus fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		unit = game.numberedUnits.get(object.get("unit").getAsInt());
		bonus = game.numberedBonuses.get(object.get("bonus").getAsInt());
		return this;
	}

}
