package ru.ancientempires.campaign.points;

import com.google.gson.JsonObject;

import ru.ancientempires.action.campaign.ActionCampaignSetNamedPoint;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetNamedPoint extends Script
{

	public String        name;
	public AbstractPoint point;

	public ScriptSetNamedPoint()
	{}

	public ScriptSetNamedPoint(String name, AbstractPoint point)
	{
		this.name = name;
		this.point = point;
	}

	@Override
	public void start()
	{
		new ActionCampaignSetNamedPoint(name, point)
				.perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		object.add("point", point.toJson());
		return object;
	}

	public ScriptSetNamedPoint fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		point = info.fromJson((JsonObject) object.get("point"), AbstractPoint.class);
		return this;
	}

}
