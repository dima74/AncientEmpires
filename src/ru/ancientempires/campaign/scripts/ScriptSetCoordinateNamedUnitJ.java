package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.NamedCoordinates;
import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
		this.name = JsonHelper.readString(reader, "name");
		this.unit = JsonHelper.readString(reader, "unit");
	}
	
	@Override
	public void start()
	{
		super.start();
		NamedCoordinates.set(this.name, NamedUnits.get(this.unit).j);
		campaign.finish(this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("name").value(this.name);
		writer.name("unit").value(this.unit);
	}
	
}
