package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.campaign.NamedCoordinates;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetCoordinateNamedUnitI extends Script
{
	
	private String name;
	private String unit;
	
	public ScriptSetCoordinateNamedUnitI()
	{}
	
	public ScriptSetCoordinateNamedUnitI(String name, String unit)
	{
		this.name = name;
		this.unit = unit;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		name = JsonHelper.readString(reader, "name");
		unit = JsonHelper.readString(reader, "unit");
	}
	
	@Override
	public void start()
	{
		NamedCoordinates.set(name, game.namedUnits.get(unit).i);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("name").value(name);
		writer.name("unit").value(unit);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		object.addProperty("unit", unit);
		return object;
	}

	public ScriptSetCoordinateNamedUnitI fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		unit = object.get("unit").getAsString();
		return this;
	}

}
