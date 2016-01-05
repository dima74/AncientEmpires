package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptUnitChangePosition extends Script
{
	private int	i;
	private int	j;
	private int	iNew;
	private int	jNew;
	
	public ScriptUnitChangePosition()
	{}
	
	public ScriptUnitChangePosition(int i, int j, int iNew, int jNew)
	{
		this.i = i;
		this.j = j;
		this.iNew = iNew;
		this.jNew = jNew;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		this.i = JsonHelper.readInt(reader, "i");
		this.j = JsonHelper.readInt(reader, "j");
		this.iNew = JsonHelper.readInt(reader, "iNew");
		this.jNew = JsonHelper.readInt(reader, "jNew");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.unitChangePosition(this.i, this.j, this.iNew, this.jNew, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(this.i);
		writer.name("j").value(this.j);
		writer.name("iNew").value(this.iNew);
		writer.name("jNew").value(this.jNew);
	}
	
}
