package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.Point;
import ru.ancientempires.action.campaign.ActionCampaignUnitMove;
import ru.ancientempires.framework.MyAssert;

public class ScriptUnitMoveExtended extends Script
{
	
	private Point[] keyPoints;
	
	public ScriptUnitMoveExtended()
	{}
	
	public ScriptUnitMoveExtended(int... keyPoints)
	{
		this.keyPoints = new Point[keyPoints.length / 2];
		for (int i = 0; i < this.keyPoints.length; i++)
			this.keyPoints[i] = new Point(keyPoints[i * 2], keyPoints[i * 2 + 1]);
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		MyAssert.a("keyPoints", reader.nextName());
		keyPoints = new Gson().fromJson(reader, Point[].class);
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitMove(keyPoints, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("keyPoints");
		new Gson().toJson(keyPoints, Point[].class, writer);
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
	
}
