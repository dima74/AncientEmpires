package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptCameraMove extends ScriptOnePoint
{
	
	public ScriptCameraMove()
	{}
	
	public ScriptCameraMove(Object... point)
	{
		super(point);
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.cameraMove(this);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptCameraMove fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
