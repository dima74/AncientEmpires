package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.action.campaign.ActionCampaignUnitMove;
import ru.ancientempires.campaign.Coordinate;
import ru.ancientempires.campaign.CoordinateInteger;

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
		i = Coordinate.getNew(reader, "i");
		j = Coordinate.getNew(reader, "j");
		targetI = Coordinate.getNew(reader, "targetI");
		targetJ = Coordinate.getNew(reader, "targetJ");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.unitMove(i.get(), j.get(), targetI.get(), targetJ.get(), this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		i.save(writer, "i");
		j.save(writer, "j");
		targetI.save(writer, "targetI");
		targetJ.save(writer, "targetJ");
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignUnitMove()
				.setIJ(i.get(), j.get())
				.setTargetIJ(targetI.get(), targetJ.get())
				.perform(game);
	}
	
}
