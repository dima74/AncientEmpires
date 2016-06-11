package ru.ancientempires.campaign.coordinates_old;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.campaign.NamedCoordinates;

public class CoordinateName extends Coordinate
{
	
	private String name;
	
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
