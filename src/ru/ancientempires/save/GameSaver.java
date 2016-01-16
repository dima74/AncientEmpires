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
	
	private GameSaveLoader	loader;
	private Game			game;
	private SaveInfo		saveInfo	= new SaveInfo();
	private GameSaverThread	thread;
	
	public GameSaver(Game game)
	{
		this.game = game;
		loader = new GameSaveLoader(game.path.getLoader(), saveInfo);
		thread = new GameSaverThread();
		thread.start();
	}
	
	public class LoadFirstSnapshot implements Save
	{
		@Override
		public void save() throws Exception
		{
			Game oldGame = game;
			game = new GameLoader(game.path).load();
			game.path = oldGame.path;
		}
	}
	
	public void initFromBase() throws IOException
	{
		FileLoader baseLoader = Client.getGame(game.path.baseGameID).getLoader();
		FileCopier copier = new FileCopier(baseLoader, loader);
		copier.copy("campaign.json", "strings.json");
		for (String lang : game.path.localizations)
			copier.copy("strings_" + lang + ".json");
			
		new SaveSnapshot().save();
		add(new LoadFirstSnapshot());
	}
	
	public void init() throws IOException
	{
		saveInfo.load();
		add(new LoadFirstSnapshot());
	}
	
	private void checkSaveSnapshot() throws IOException
	{
		if (saveInfo.numberActionsAfterLastSave == 100)
			saveSnapshot();
	}
	
	public class SaveSnapshot implements Save
	{
		@Override
		public void save() throws IOException
		{
			System.out.println(game.players[0].units);
			
			++saveInfo.numberSnapshots;
			loader.snapshots().mkdirs("");
			loader.actions().mkdirs("");
			saveInfo.numberActionsAfterLastSave = 0;
			game.campaign = Client.getGame().campaign;
			new GameSnapshotSaver(game, loader.snapshots()).save();
			saveInfo.save();
		}
	}
	
	public void saveSnapshot() throws IOException
	{
		System.out.println("GameSaver.saveSnapshot()");
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
			action.game = game;
			action.performQuick();
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
		System.out.println("GameSaver.save()");
		add(new SaveAction(action));
	}
	
	public void finishSave() throws InterruptedException
	{
		thread.isRunning = false;
		thread.join();
	}
	
	private void add(Save save)
	{
		thread.queue.offer(save);
	}
	
}
