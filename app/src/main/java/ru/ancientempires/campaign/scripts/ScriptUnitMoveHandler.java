package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.serializable.LoaderInfo;

public abstract class ScriptUnitMoveHandler extends Script
{
	
	public boolean complete;
	
	public abstract void unitMove(UnitBitmap unit);
	
	public void complete()
	{
		if (!complete)
		{
			complete = true;
			campaign.iDrawCampaign.updateCampaign();
		}
	}
	
	@Override
	public boolean check()
	{
		return super.check() && complete;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("complete", complete);
		return object;
	}

	public ScriptUnitMoveHandler fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		complete = object.get("complete").getAsBoolean();
		return this;
	}

}
