package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ConditionNamedUnitDead extends Condition
{

	private String name;

	public ConditionNamedUnitDead()
	{}

	public ConditionNamedUnitDead(String name)
	{
		this.name = name;
	}

	@Override
	public boolean check()
	{
		Unit unit = game.namedUnits.get(name);
		return game.floatingUnit != unit && game.getUnit(unit.i, unit.j) != unit;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		return object;
	}

	public ConditionNamedUnitDead fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		return this;
	}

}
