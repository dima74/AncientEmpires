package ru.ancientempires.campaign.coordinate;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.helpers.JsonHelper;

public class CoordinateNamedUnitJ extends Coordinate
{
	
	private String name;
	
	public CoordinateNamedUnitJ()
	{}
	
	public CoordinateNamedUnitJ(String name)
	{
		this.name = name;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		name = JsonHelper.readString(reader, "name");
	}
	
	@Override
	public int get()
	{
		return NamedUnits.get().get(name).j;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("name").value(name);
	}
	
}
