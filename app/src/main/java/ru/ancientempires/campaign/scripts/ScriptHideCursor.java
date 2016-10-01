package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptHideCursor extends Script
{

	@Override
	public void start()
	{
		campaign.iDrawCampaign.hideCursor(this);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptHideCursor fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
