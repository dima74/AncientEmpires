package ru.ancientempires.campaign.coordinates_old;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CoordinateInteger extends Coordinate
{
	
	private int value;
	
	public CoordinateInteger(int value)
	{
		this.value = value;
	}
	
	@Override
	public int get()
	{
		return this.value;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.value(this.value);
	}
	
}
