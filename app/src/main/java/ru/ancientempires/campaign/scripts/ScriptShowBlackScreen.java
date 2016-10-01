package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptShowBlackScreen extends Script
{

	@Override
	public void start()
	{
		campaign.iDrawCampaign.showBlackScreen(this);
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
		return object;
	}

	public ScriptShowBlackScreen fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
