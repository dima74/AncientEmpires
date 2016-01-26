package ru.ancientempires.save;

import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.Action;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.load.GameLoader;
import ru.ancientempires.model.Game;

public class GameSaver
{
	
	public GameSaveLoader	loader;
	public Game				game;
	public Game				mainGame;
	public SaveInfo			saveInfo	= new SaveInfo();
	public GameSaverThread	thread;
	
	public GameSaver(Game game)
	{
		mainGame = game;
		loader = new GameSaveLoader(game.path.getLoader(), saveInfo);
		thread = new GameSaverThread();
		thread.start();
	}
	
	public class LoadFirstSnapshot implements Save
	{
		@Override
		public void save() throws Exception
		{
			game = new GameLoader(mainGame.path).load();
			game.path = mainGame.path;
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
			
		game = mainGame;
		new SaveSnapshot().save();
		game = null;
		add(new LoadFirstSnapshot());
	}
	
	public void init() throws IOException
	{
		saveInfo.load();
		add(new LoadFirstSnapshot());
	}
	
	public void checkSaveSnapshot() throws IOException
	{
		if (saveInfo.numberActionsAfterLastSave == 100)
			saveSnapshot();
	}
	
	public class SaveSnapshot implements Save
	{
		@Override
		public void save() throws IOException
		{
			++saveInfo.numberSnapshots;
			loader.snapshots().mkdirs("");
			loader.actions().mkdirs("");
			saveInfo.numberActionsAfterLastSave = 0;
			game.campaign = mainGame.campaign;
			new GameSnapshotSaver(game, loader.snapshots()).save();
			saveInfo.save();
		}
	}
	
	public void saveSnapshot() throws IOException
	{
		add(new SaveSnapshot());
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
			// System.out.println("\t\t" + action);
			action.checkBase(game);
			action.performQuickBase(game);
			if (!action.isCampaign())
			{
				String relativeNumberAction = "" + saveInfo.numberActionsAfterLastSave++;
				DataOutputStream dos = loader.actions().openDOS(relativeNumberAction);
				action.saveBase(dos);
				dos.close();
				saveInfo.save();
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
		while (!thread.queue.isEmpty())
			Thread.yield();
	}
	
	public void finishSave() throws InterruptedException
	{
		thread.isRunning = false;
		thread.join();
	}
	
	public void add(Save save)
	{
		thread.queue.offer(save);
	}
	
}
