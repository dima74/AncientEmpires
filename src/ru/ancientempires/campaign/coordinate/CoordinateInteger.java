package ru.ancientempires.campaign.coordinate;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class CoordinateInteger extends Coordinate
{
	
	private int	value;
	
	public CoordinateInteger(int value)
	{
		this.value = value;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{}
	
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
