package ru.ancientempires.save;

import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.Action;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.load.GameLoader;
import ru.ancientempires.model.Game;

public class GameSaver
{
	
	public GameSaveLoader	loader;
	public Game				game;
	public Game				mainGame;
	public GameSaverThread	thread;
	
	public GameSaver(Game game)
	{
		mainGame = game;
		loader = new GameSaveLoader(game.path.getLoader());
		thread = new GameSaverThread();
		thread.start();
	}
	
	public class LoadFirstSnapshot implements Save
	{
		@Override
		public void save() throws Exception
		{
			game = new GameLoader(mainGame.path, mainGame.rules).load(false);
			game.path = mainGame.path;
			game.path.canChooseTeams = false;
			// game.campaign = mainGame.campaign.createSimpleCopy(game);
			game.isSaver = true;
		}
	}
	
	public void initFromBase() throws IOException
	{
		FileLoader baseLoader = Client.getGame(mainGame.path.baseGameID).getLoader();
		FileCopier copier = new FileCopier(baseLoader, loader);
		copier.copy("campaign.json", "strings.json");
		for (String lang : mainGame.path.localizations)
			copier.copy("strings_" + lang + ".json");
			
		new SaveSnapshot().save(mainGame);
		add(new LoadFirstSnapshot());
	}
	
	public void init() throws IOException
	{
		loader.load();
		add(new LoadFirstSnapshot());
	}
	
	// всё, кроме strings
	public void saveBaseGame() throws IOException
	{
		new SaveSnapshot().save(mainGame);
		if (!mainGame.campaign.isDefault)
			mainGame.campaign.save(loader);
		thread.stopRunning();
	}
	
	public void checkSaveSnapshot() throws IOException
	{
		if (loader.numberActionsAfterLastSave == 100)
			saveSnapshot();
	}
	
	public class SaveSnapshot implements Save
	{
		private Campaign campaign;
		
		public SaveSnapshot()
		{}
		
		public SaveSnapshot(Campaign campaign)
		{
			this.campaign = campaign;
		}
		
		@Override
		public void save() throws IOException
		{
			save(game);
		}
		
		public void save(Game game) throws IOException
		{
			++loader.numberSnapshots;
			loader.snapshots().mkdirs();
			loader.actions().mkdirs();
			loader.numberActionsAfterLastSave = 0;
			if (campaign != null)
				game.campaign = campaign;
			new GameSnapshotSaver(game, loader.snapshots()).save();
			// game.lastTime =
			game.path.save();
			loader.save();
		}
	}
	
	public void saveSnapshot() throws IOException
	{
		add(new SaveSnapshot(mainGame.campaign.createSimpleCopy(game)));
	}
	
	public class SaveAction implements Save
	{
		public Action action;
		
		public SaveAction(Action action)
		{
			this.action = action;
		}
		
		@Override
		public void save() throws IOException
		{
			action.checkBase(game);
			action.performQuickBase(game);
			if (!action.isCampaign())
			{
				String relativeNumberAction = "" + loader.numberActionsAfterLastSave++;
				DataOutputStream dos = loader.actions().openDOS(relativeNumberAction);
				action.saveBase(dos);
				dos.close();
				loader.save();
				checkSaveSnapshot();
			}
		}
	}
	
	public void save(Action action) throws IOException
	{
		add(new SaveAction(action));
	}
	
	public void waitSave() throws InterruptedException
	{
		MyAssert.a(thread.isAlive());
		while (!thread.queue.isEmpty())
			Thread.yield();
	}
	
	public void finishSave() throws InterruptedException
	{
		thread.stopRunning();
		thread.join();
	}
	
	public void add(Save save)
	{
		thread.queue.offer(save);
	}
	
}
