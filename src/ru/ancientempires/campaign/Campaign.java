package ru.ancientempires.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Game;

public class Campaign
{
	
	public Game game;
	
	public Script[]			scripts;
	public IDrawCampaign	iDrawCampaign;
	public boolean			isDefault	= false;
	
	public Campaign(Game game)
	{
		this.game = game;
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
		for (Script script : game.campaign.scripts)
		{
			output.writeBoolean(script.isStarting);
			output.writeBoolean(script.isFinishing);
		}
		output.close();
	}
	
	public void loadState(FileLoader loader) throws IOException
	{
		DataInputStream input = loader.openDIS("campaignState.dat");
		for (Script script : game.campaign.scripts)
		{
			script.isStarting = input.readBoolean();
			script.isFinishing = input.readBoolean();
		}
		input.close();
	}
	
}
