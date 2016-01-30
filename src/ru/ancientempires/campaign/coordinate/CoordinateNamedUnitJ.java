package ru.ancientempires.campaign.coordinate;

import java.io.IOException;

import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class CoordinateNamedUnitJ extends Coordinate
{
	
	private String	name;
	
	public CoordinateNamedUnitJ()
	{}
	
	public CoordinateNamedUnitJ(String name)
	{
		this.name = name;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		this.name = JsonHelper.readString(reader, "name");
	}
	
	@Override
	public int get()
	{
		return NamedUnits.get(this.name).j;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("name").value(this.name);
	}
	
}