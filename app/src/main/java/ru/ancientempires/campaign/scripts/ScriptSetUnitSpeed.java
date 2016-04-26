package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;

public class ScriptSetUnitSpeed extends Script
{
	
	private int framesForCell;
	
	public ScriptSetUnitSpeed()
	{}
	
	public ScriptSetUnitSpeed(int framesForCell)
	{
		this.framesForCell = framesForCell;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		framesForCell = JsonHelper.readInt(reader, "framesForCell");
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.setUnitSpeed(framesForCell, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("framesForCell").value(framesForCell);
	}
	
}
