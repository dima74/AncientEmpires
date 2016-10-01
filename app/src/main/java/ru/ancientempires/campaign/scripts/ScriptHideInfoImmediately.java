package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptHideInfoImmediately extends Script
{

	@Override
	public void start()
	{
		campaign.iDrawCampaign.hideInfoImmediately(this);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptHideInfoImmediately fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
