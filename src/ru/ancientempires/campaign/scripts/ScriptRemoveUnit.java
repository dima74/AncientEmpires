package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.action.campaign.ActionCampaignRemoveUnit;
import ru.ancientempires.campaign.Coordinate;
import ru.ancientempires.campaign.CoordinateInteger;

public class ScriptRemoveUnit extends Script
{
	
	private Coordinate	i;
	private Coordinate	j;
	
	public ScriptRemoveUnit()
	{}
	
	public ScriptRemoveUnit(Coordinate i, Coordinate j)
	{
		this.i = i;
		this.j = j;
	}
	
	public ScriptRemoveUnit(int i, int j)
	{
		this.i = new CoordinateInteger(i);
		this.j = new CoordinateInteger(j);
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		i = Coordinate.getNew(reader, "i");
		j = Coordinate.getNew(reader, "j");
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.removeUnit(i.get(), j.get(), this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		i.save(writer, "i");
		j.save(writer, "j");
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignRemoveUnit()
				.setIJ(i.get(), j.get())
				.perform(game);
	}
	
}
