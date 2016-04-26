package ru.ancientempires.campaign;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Game;
import ru.ancientempires.reflection.LoaderInfo;
import ru.ancientempires.reflection.ReflectionLoader;
import ru.ancientempires.reflection.ReflectionSaver;

public class Campaign
{
	
	public Game game;

	public Script[]      scripts;
	public IDrawCampaign iDrawCampaign;
	public boolean isDefault = false;

	public Campaign(Game game)
	{
		this.game = game;
	}
	
	public void save(FileLoader loader) throws Exception
	{
		JsonWriter writer = loader.getWriter("campaign.json");
		ReflectionSaver.save(writer, scripts);
		writer.close();
	}
	
	public void load(FileLoader loader) throws Exception
	{
		JsonReader reader = loader.getReader("campaign.json");
		scripts = ReflectionLoader.load(reader, Script[].class, new LoaderInfo(game));
		reader.close();

		for (Script script : scripts)
		{
			script.campaign = this;
			script.game = game;
			script.resolveAliases(scripts);
		}
	}
	
	public void start()
	{
		update();
	}
	
	public boolean isUpdate;
	public boolean needSaveSnapshot;

	public void update()
	{
		if (isUpdate)
			return;
		isUpdate = true;
		boolean change = true;
		while (change)
		{
			change = false;
			for (Script script : scripts)
				if (!script.isStarting && script.checkGeneral())
				{
					change = true;
					script.isStarting = true;
					script.start();
					if (script.isSimple())
						script.isFinishing = true;
				}
		}

		isUpdate = false;
		if (needSaveSnapshot)
		{
			try
			{
				game.saver.saveSnapshot();
			} catch (IOException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
			needSaveSnapshot = false;
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
		public SimpleScript()
		{
		}

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
