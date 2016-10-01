package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.model.NamedCoordinates;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetCoordinateNamedUnitJ extends Script
{

	private String name;
	private String unit;

	public ScriptSetCoordinateNamedUnitJ()
	{}

	public ScriptSetCoordinateNamedUnitJ(String name, String unit)
	{
		this.name = name;
		this.unit = unit;
	}

	@Override
	public void start()
	{
		NamedCoordinates.set(name, game.namedUnits.get(unit).j);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		object.addProperty("unit", unit);
		return object;
	}

	public ScriptSetCoordinateNamedUnitJ fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		unit = object.get("unit").getAsString();
		return this;
	}

}
