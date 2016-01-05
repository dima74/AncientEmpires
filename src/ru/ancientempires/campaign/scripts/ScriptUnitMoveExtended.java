package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.Point;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.framework.MyAssert;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptUnitMoveExtended extends Script
{
	
	private Point[]	keyPoints;
	
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
		this.keyPoints = new Gson().fromJson(reader, Point[].class);
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.unitMove(this.keyPoints, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("keyPoints");
		new Gson().toJson(this.keyPoints, Point[].class, writer);
	}
	
}
