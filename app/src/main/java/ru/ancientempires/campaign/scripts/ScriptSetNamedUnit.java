package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignSetNamedUnit;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetNamedUnit extends Script
{
	
	private int    i;
	private int    j;
	private String name;
	
	public ScriptSetNamedUnit()
	{}
	
	public ScriptSetNamedUnit(int i, int j, String name)
	{
		this.i = i;
		this.j = j;
		this.name = name;
	}
	
	@Override
	public void start()
	{
		new ActionCampaignSetNamedUnit(name)
				.setIJ(i, j)
				.perform(game);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("i", i);
		object.addProperty("j", j);
		object.addProperty("name", name);
		return object;
	}

	public ScriptSetNamedUnit fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		name = object.get("name").getAsString();
		return this;
	}

}
