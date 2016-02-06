package ru.ancientempires.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Game;

public class Campaign
{
	
	public Game game;
	
	@Expose
	public Script[]			scripts;
	public IDrawCampaign	iDrawCampaign;
	public boolean			isDefault	= false;
	
	public Campaign(Game game)
	{
		this.game = game;
	}
	
	public void save(FileLoader loader) throws IOException
	{
		// TODO переписать методы во всех скриптах, чтобы принимали JsonObject и добавляли в него свойства
		/*
		JsonWriter writer = loader.getWriter("campaign.json");
		new Gson().toJson(this, Campaign.class, writer);
		writer.close();
		*/
		
		JsonWriter writer = loader.getWriter("campaign.json");
		writer.beginObject();
		writer.name("scripts").beginArray();
		for (Script script : scripts)
			script.saveGeneral(writer);
		writer.endArray();
		writer.endObject();
		writer.close();
	}
	
	public void load(FileLoader loader) throws Exception
	{
		scripts = new CampaignLoader(loader, game, this).load();
	}
	
	public void start()
	{
		update();
	}
	
	public void update()
	{
		boolean change = true;
		while (change)
		{
			change = false;
			for (Script script : scripts)
				if (!script.isStarting && script.checkGeneral())
				{
					change = true;
					script.start();
					if (script.type.isSimple)
						script.isFinishing = true;
				}
		}
	}
	
	public void finish(Script script)
	{
		script.isFinishing = true;
		iDrawCampaign.updateCampaign();
	}
	
	public void saveState(FileLoader loader) throws IOException
	{
		DataOutputStream output = loader.openDOS("campaignState.dat");
		for (Script script : scripts)
		{
			output.writeBoolean(script.isStarting);
			output.writeBoolean(script.isFinishing);
		}
		output.close();
	}
	
	public void loadState(FileLoader loader) throws IOException
	{
		DataInputStream input = loader.openDIS("campaignState.dat");
		for (Script script : scripts)
		{
			script.isStarting = input.readBoolean();
			script.isFinishing = input.readBoolean();
		}
		input.close();
	}
	
	private static class SimpleScript extends Script
	{
		public SimpleScript(Script script)
		{
			isStarting = script.isStarting;
			isFinishing = script.isFinishing;
		}
	}
	
	public Campaign createSimpleCopy(Game otherGame)
	{
		Campaign campaign = new Campaign(otherGame);
		campaign.scripts = new Script[scripts.length];
		for (int i = 0; i < scripts.length; i++)
			campaign.scripts[i] = new SimpleScript(scripts[i]);
		return campaign;
	}
	
}
