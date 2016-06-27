package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignLeaveCampaign;
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
		new ActionCampaignLeaveCampaign().perform(game);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
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
