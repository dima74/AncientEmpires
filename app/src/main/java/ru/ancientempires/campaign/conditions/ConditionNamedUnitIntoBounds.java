package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class ConditionNamedUnitIntoBounds extends Condition
{
	
	private String           name;
	private AbstractBounds[] bounds;
	
	public ConditionNamedUnitIntoBounds()
	{
	}
	
	public ConditionNamedUnitIntoBounds(String name, AbstractBounds... bounds)
	{
		this.name = name;
		this.bounds = bounds;
	}
	
	@Override
	public boolean check()
	{
		Unit unit = game.namedUnits.get(name);
		for (AbstractBounds bounds : this.bounds)
			if (bounds.in(unit.i, unit.j))
				return true;
		return false;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		object.add("bounds", SerializableJsonHelper.toJsonArray(bounds));
		return object;
	}

	public ConditionNamedUnitIntoBounds fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		bounds = AbstractBounds.fromJsonArray(object.get("bounds").getAsJsonArray(), info);
		return this;
	}

}
