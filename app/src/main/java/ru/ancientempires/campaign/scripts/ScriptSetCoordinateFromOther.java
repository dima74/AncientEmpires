package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.model.NamedCoordinates;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetCoordinateFromOther extends Script
{

	private String otherName;
	private String name;
	private int    delta;

	public ScriptSetCoordinateFromOther()
	{}

	public ScriptSetCoordinateFromOther(String otherName, String name, int delta)
	{
		this.otherName = otherName;
		this.name = name;
		this.delta = delta;
	}

	@Override
	public void start()
	{
		NamedCoordinates.set(name, NamedCoordinates.get(otherName) + delta);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("otherName", otherName);
		object.addProperty("name", name);
		object.addProperty("delta", delta);
		return object;
	}

	public ScriptSetCoordinateFromOther fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		otherName = object.get("otherName").getAsString();
		name = object.get("name").getAsString();
		delta = object.get("delta").getAsInt();
		return this;
	}

}
