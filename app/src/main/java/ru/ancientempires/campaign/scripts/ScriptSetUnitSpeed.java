package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;

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
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("framesForCell", framesForCell);
		return object;
	}

	public ScriptSetUnitSpeed fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		framesForCell = object.get("framesForCell").getAsInt();
		return this;
	}

}
