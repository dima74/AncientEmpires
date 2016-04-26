package ru.ancientempires.campaign.scripts;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.helpers.JsonHelper;

public class ScriptDelay extends Script
{
	
	public int milliseconds;
	
	public ScriptDelay()
	{}
	
	public ScriptDelay(int milliseconds)
	{
		this.milliseconds = milliseconds;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		milliseconds = JsonHelper.readInt(reader, "milliseconds");
	}
	
	@Override
	public void start()
	{
		//campaign.iDrawCampaign.delay(milliseconds, this);
		finish();
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("milliseconds").value(milliseconds);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
}
