package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;

public class ScriptSparkDefault extends Script
{
	
	private int	i;
	private int	j;
	
	public ScriptSparkDefault()
	{}
	
	public ScriptSparkDefault(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		i = JsonHelper.readInt(reader, "i");
		j = JsonHelper.readInt(reader, "j");
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.sparksDefault(i, j, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(i);
		writer.name("j").value(j);
	}
	
}
