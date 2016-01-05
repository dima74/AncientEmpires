package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.Coordinate;
import ru.ancientempires.campaign.CoordinateInteger;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptCameraMove extends Script
{
	
	private Coordinate	i;
	private Coordinate	j;
	
	public ScriptCameraMove()
	{}
	
	public ScriptCameraMove(Coordinate i, Coordinate j)
	{
		this.i = i;
		this.j = j;
	}
	
	public ScriptCameraMove(int i, int j)
	{
		this.i = new CoordinateInteger(i);
		this.j = new CoordinateInteger(j);
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		this.i = Coordinate.getNew(reader, "i");
		this.j = Coordinate.getNew(reader, "j");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.cameraMove(this.i.get(), this.j.get(), this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		this.i.save(writer, "i");
		this.j.save(writer, "j");
	}
	
}
