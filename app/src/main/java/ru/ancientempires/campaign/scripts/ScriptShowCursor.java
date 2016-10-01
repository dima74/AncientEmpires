package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptShowCursor extends Script
{

	@Override
	public void start()
	{
		campaign.iDrawCampaign.showCursor(this);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptShowCursor fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
