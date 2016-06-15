package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSparkDefault extends Script
{
	
	private int i;
	private int j;
	
	public ScriptSparkDefault()
	{}
	
	public ScriptSparkDefault(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.sparksDefault(i, j, this);
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
		object.addProperty("i", i);
		object.addProperty("j", j);
		return object;
	}

	public ScriptSparkDefault fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		return this;
	}

}
