package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptCitadelAttack extends ScriptOnePoint
{

	public ScriptCitadelAttack()
	{}

	public ScriptCitadelAttack(Object... point)
	{
		super(point);
	}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.citadelAttack(this);
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

	public ScriptCitadelAttack fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
