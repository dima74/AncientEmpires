package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.campaign.NamedCoordinates;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptSetCoordinateNamedUnitJ extends Script
{
	
	private String	name;
	private String	unit;
	
	public ScriptSetCoordinateNamedUnitJ()
	{}
	
	public ScriptSetCoordinateNamedUnitJ(String name, String unit)
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
		NamedCoordinates.set(name, game.namedUnits.get(unit).j);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("name").value(name);
		writer.name("unit").value(unit);
	}
	
}
