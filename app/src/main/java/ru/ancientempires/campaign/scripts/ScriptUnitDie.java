package ru.ancientempires.campaign.scripts;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.action.campaign.ActionCampaignRemoveUnit;
import ru.ancientempires.helpers.JsonHelper;

public class ScriptUnitDie extends Script
{
	
	private int i;
	private int j;
	
	public ScriptUnitDie()
	{
	}
	
	public ScriptUnitDie(int i, int j)
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
		campaign.iDrawCampaign.unitDie(i, j, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(i);
		writer.name("j").value(j);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignRemoveUnit()
				.setIJ(i, j)
				.perform(game);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}

}
