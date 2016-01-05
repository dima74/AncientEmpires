package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.Coordinate;
import ru.ancientempires.campaign.CoordinateInteger;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptUnitMove extends Script
{
	
	private Coordinate	i;
	private Coordinate	j;
	private Coordinate	targetI;
	private Coordinate	targetJ;
	
	public ScriptUnitMove()
	{}
	
	public ScriptUnitMove(Coordinate i, Coordinate j, Coordinate targetI, Coordinate targetJ)
	{
		this.i = i;
		this.j = j;
		this.targetI = targetI;
		this.targetJ = targetJ;
	}
	
	public ScriptUnitMove(int i, int j, int targetI, int targetJ)
	{
		this.i = new CoordinateInteger(i);
		this.j = new CoordinateInteger(j);
		this.targetI = new CoordinateInteger(targetI);
		this.targetJ = new CoordinateInteger(targetJ);
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		this.i = Coordinate.getNew(reader, "i");
		this.j = Coordinate.getNew(reader, "j");
		this.targetI = Coordinate.getNew(reader, "targetI");
		this.targetJ = Coordinate.getNew(reader, "targetJ");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.unitMove(this.i.get(), this.j.get(), this.targetI.get(), this.targetJ.get(), this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		this.i.save(writer, "i");
		this.j.save(writer, "j");
		this.targetI.save(writer, "targetI");
		this.targetJ.save(writer, "targetJ");
	}
	
}
