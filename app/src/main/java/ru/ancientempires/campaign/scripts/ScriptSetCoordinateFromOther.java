package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.campaign.NamedCoordinates;
import ru.ancientempires.helpers.JsonHelper;
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
	public void load(JsonReader reader) throws IOException
	{
		otherName = JsonHelper.readString(reader, "otherName");
		name = JsonHelper.readString(reader, "name");
		delta = JsonHelper.readInt(reader, "delta");
	}
	
	@Override
	public void start()
	{
		NamedCoordinates.set(name, NamedCoordinates.get(otherName) + delta);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("otherName").value(otherName);
		writer.name("name").value(name);
		writer.name("delta").value(delta);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
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
