package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;

public class ScriptSetCameraSpeed extends Script
{
	
	private int delta;
	
	public ScriptSetCameraSpeed()
	{}
	
	public ScriptSetCameraSpeed(int delta)
	{
		this.delta = delta;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		delta = JsonHelper.readInt(reader, "delta");
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.setCameraSpeed(delta, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("delta").value(delta);
	}
	
}
