package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;

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
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("milliseconds", milliseconds);
		return object;
	}

	public ScriptDelay fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		milliseconds = object.get("milliseconds").getAsInt();
		return this;
	}

}
