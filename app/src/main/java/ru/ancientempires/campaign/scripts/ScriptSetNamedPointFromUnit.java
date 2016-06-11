package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.action.campaign.ActionCampaignSetNamedPoint;
import ru.ancientempires.campaign.points.PointInteger;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetNamedPointFromUnit extends Script
{

	public String pointName;
	public String unitName;

	public ScriptSetNamedPointFromUnit()
	{
	}

	public ScriptSetNamedPointFromUnit(String pointName, String unitName)
	{
		this.pointName = pointName;
		this.unitName = unitName;
	}

	@Override
	public void start()
	{
		Unit unit = game.namedUnits.get(unitName);
		new ActionCampaignSetNamedPoint(pointName, new PointInteger(unit.i, unit.j))
				.perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("pointName", pointName);
		object.addProperty("unitName", unitName);
		return object;
	}

	public ScriptSetNamedPointFromUnit fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		pointName = object.get("pointName").getAsString();
		unitName = object.get("unitName").getAsString();
		return this;
	}

}
