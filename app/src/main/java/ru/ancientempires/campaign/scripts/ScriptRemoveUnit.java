package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignRemoveUnit;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptRemoveUnit extends ScriptOnePoint
{

	public ScriptRemoveUnit()
	{}

	public ScriptRemoveUnit(Object... point)
	{
		super(point);
	}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.removeUnit(i(), j(), this);
	}

	@Override
	public void performAction()
	{
		new ActionCampaignRemoveUnit()
				.setIJ(i(), j())
				.perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptRemoveUnit fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
