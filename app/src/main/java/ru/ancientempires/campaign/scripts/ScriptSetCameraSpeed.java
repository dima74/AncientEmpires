package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;

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
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("delta", delta);
		return object;
	}

	public ScriptSetCameraSpeed fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		delta = object.get("delta").getAsInt();
		return this;
	}

}
