package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptEnableActiveGame extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.enableActiveGame(this);
	}
	
	@Override
	public void performAction()
	{
		campaign.needActionRewriteScriptsStatus = true;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptEnableActiveGame fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
