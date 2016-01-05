package ru.ancientempires.campaign;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

public class CoordinateName extends Coordinate
{
	
	private String	name;
	
	public CoordinateName(String name)
	{
		this.name = name;
	}
	
	@Override
	public int get()
	{
		return NamedCoordinates.get(this.name);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.value(this.name);
	}
	
}
