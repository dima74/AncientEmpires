package ru.ancientempires.campaign;

import java.io.IOException;

import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Game;

public class Campaign
{
	
	public Game game;
	
	public Script[]			scripts;		// all all
	public IDrawCampaign	iDrawCampaign;
	
	public void load(FileLoader loader, Game game) throws IOException
	{
		this.game = game;
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
	
}
