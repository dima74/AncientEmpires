package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptUnitMoveExtended extends Script
{
	/*
	public Point[]	keyPoints;
	public boolean	makeSmoke	= true;
								
	public ScriptUnitMoveExtended()
	{}
	
	public ScriptUnitMoveExtended(int... keyPoints)
	{
		this.keyPoints = new Point[keyPoints.length / 2];
		for (int i = 0; i < this.keyPoints.length; i++)
			this.keyPoints[i] = new Point(keyPoints[i * 2], keyPoints[i * 2 + 1]);
	}
	
	public ScriptUnitMoveExtended disableMakeSmoke()
	{
		makeSmoke = false;
		return this;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		MyAssert.a("keyPoints", reader.nextName());
		keyPoints = new Gson().fromJson(reader, Point[].class);
		makeSmoke = JsonHelper.readBoolean(reader, "makeSmoke");
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitMove(this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("keyPoints");
		new Gson().toJson(keyPoints, Point[].class, writer);
		writer.name("makeSmoke").value(makeSmoke);
	}
	
	@Override
	public void performAction()
	{
		Point start = keyPoints[0];
		Point end = keyPoints[keyPoints.length - 1];
		new ActionCampaignUnitMove()
				.setIJ(start.i, start.j)
				.setTargetIJ(end.i, end.j)
				.perform(game);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}//*/

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptUnitMoveExtended fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
