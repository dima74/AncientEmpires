package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

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
	public void start()
	{
		//campaign.iDrawCampaign.delay(milliseconds, this);
		finish();
	}
	
	@Override
	public boolean isSimple()
	{
		return false;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
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
