package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.NamedCoordinates;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptSetCoordinateFromOther extends Script
{
	
	private String	otherName;
	private String	name;
	private int		delta;
	
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
		this.otherName = JsonHelper.readString(reader, "otherName");
		this.name = JsonHelper.readString(reader, "name");
		this.delta = JsonHelper.readInt(reader, "delta");
	}
	
	@Override
	public void start()
	{
		super.start();
		NamedCoordinates.set(this.name, NamedCoordinates.get(this.otherName) + this.delta);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("otherName").value(this.otherName);
		writer.name("name").value(this.name);
		writer.name("delta").value(this.delta);
	}
	
}
