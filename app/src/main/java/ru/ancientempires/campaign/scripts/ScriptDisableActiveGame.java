package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignEnterCampaign;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptDisableActiveGame extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.disableActiveGame(this);
	}

	@Override
	public void performAction()
	{
		new ActionCampaignEnterCampaign().perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptDisableActiveGame fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
