package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import java.util.Arrays;

import ru.ancientempires.actions.campaign.ActionCampaignUnitChangePosition;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.MyNullable;

public class ScriptUnitMove extends Script
{
	
	public AbstractPoint[] points;
	@MyNullable
	public Script[]        handlers; // не ScriptUnitMoveHandler, чтобы можно было присваивать ScriptAlias
	public boolean makeSmoke = true;

	public ScriptUnitMove()
	{}
	
	public ScriptUnitMove(Object... points)
	{
		this.points = AbstractPoint.createPoints(points);
	}

	public ScriptUnitMove setHandlers(ScriptUnitMoveHandler... handlers)
	{
		this.handlers = handlers;
		return this;
	}

	public ScriptUnitMove disableMakeSmoke()
	{
		makeSmoke = false;
		return this;
	}

	public void resolveAliases(Script[] scripts)
	{
		super.resolveAliases(scripts);
		if (handlers != null)
			resolveAliases(handlers, scripts);
	}
	
	private AbstractPoint first()
	{
		return points[0];
	}
	
	private AbstractPoint last()
	{
		return points[points.length - 1];
	}
	
	public int i()
	{
		return first().getI();
	}
	
	public int j()
	{
		return first().getJ();
	}

	public int targetI()
	{
		return last().getI();
	}
	
	public int targetJ()
	{
		return last().getJ();
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitMove(this, true);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignUnitChangePosition()
				.setIJ(i(), j())
				.setTargetIJ(targetI(), targetJ())
				.perform(game);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return "ScriptUnitMove{" +
				"points=" + Arrays.toString(points) +
				", handlers=" + handlers +
				", makeSmoke=" + makeSmoke +
				'}';
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.add("points", ru.ancientempires.serializable.SerializableJsonHelper.toJsonArray(points));
		if (handlers != null)
			object.add("handlers", ru.ancientempires.serializable.SerializableJsonHelper.toJsonArrayNumbered(handlers));
		object.addProperty("makeSmoke", makeSmoke);
		return object;
	}

	public ScriptUnitMove fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		points = AbstractPoint.fromJsonArray(object.get("points").getAsJsonArray(), info);
		if (object.has("handlers"))
			handlers = Script.newInstanceArrayNumbered(object.get("handlers").getAsJsonArray(), info);
		makeSmoke = object.get("makeSmoke").getAsBoolean();
		return this;
	}

}
