package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptSetCameraSpeed extends Script
{
	
	private int	delta;
	
	public ScriptSetCameraSpeed()
	{}
	
	public ScriptSetCameraSpeed(int delta)
	{
		this.delta = delta;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		this.delta = JsonHelper.readInt(reader, "delta");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.setCameraSpeed(this.delta, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("delta").value(this.delta);
	}
	
}
