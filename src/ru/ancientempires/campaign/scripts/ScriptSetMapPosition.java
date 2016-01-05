package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
		this.i = JsonHelper.readInt(reader, "i");
		this.j = JsonHelper.readInt(reader, "j");
	}
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.setMapPosition(this.i, this.j, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(this.i);
		writer.name("j").value(this.j);
	}
	
}
