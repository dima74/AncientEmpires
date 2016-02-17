package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;

public class ScriptSetMapPosition extends Script
{
	
	private int	i;
	private int	j;
	
	public ScriptSetMapPosition()
	{}
	
	public ScriptSetMapPosition(int i, int j)
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
		campaign.iDrawCampaign.setMapPosition(i, j, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(i);
		writer.name("j").value(j);
	}
	
}
