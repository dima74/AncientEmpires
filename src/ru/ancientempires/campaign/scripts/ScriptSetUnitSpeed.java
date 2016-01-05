package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptSetUnitSpeed extends Script
{
	
	private int	framesForCell;
	
	public ScriptSetUnitSpeed()
	{}
	
	public ScriptSetUnitSpeed(int framesForCell)
	{
		this.framesForCell = framesForCell;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		this.framesForCell = JsonHelper.readInt(reader, "framesForCell");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.setUnitSpeed(this.framesForCell, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("framesForCell").value(this.framesForCell);
	}
	
}
