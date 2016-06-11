package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.action.campaign.ActionCampaignUnitChangePosition;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptUnitChangePosition extends Script
{
	
	private int i;
	private int j;
	private int targetI;
	private int targetJ;
	
	public ScriptUnitChangePosition()
	{}
	
	public ScriptUnitChangePosition(int i, int j, int targetI, int targetJ)
	{
		this.i = i;
		this.j = j;
		this.targetI = targetI;
		this.targetJ = targetJ;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		i = JsonHelper.readInt(reader, "i");
		j = JsonHelper.readInt(reader, "j");
		targetI = JsonHelper.readInt(reader, "targetI");
		targetJ = JsonHelper.readInt(reader, "targetJ");
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitChangePosition(i, j, targetI, targetJ, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(i);
		writer.name("j").value(j);
		writer.name("targetI").value(targetI);
		writer.name("targetJ").value(targetJ);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignUnitChangePosition()
				.setIJ(i, j)
				.setTargetIJ(targetI, targetJ)
				.perform(game);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("i", i);
		object.addProperty("j", j);
		object.addProperty("targetI", targetI);
		object.addProperty("targetJ", targetJ);
		return object;
	}

	public ScriptUnitChangePosition fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		targetI = object.get("targetI").getAsInt();
		targetJ = object.get("targetJ").getAsInt();
		return this;
	}

}
