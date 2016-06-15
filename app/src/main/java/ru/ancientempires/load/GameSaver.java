package ru.ancientempires.load;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.ancientempires.actions.Action;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;

public class GameSaver
{

	public Game            mainGame;
	public Game            game;
	public GameSaverThread thread;

	public FileLoader       loader;
	public FileOutputStream actionsFOS;
	public DataOutputStream actionsDOS;
	//public PrintWriter      snapshotsWriter;

	public GameSaver(Game mainGame) throws Exception
	{
		this.mainGame = mainGame;
		mainGame.path.addNoteInitial(mainGame);
		game = mainGame.myClone();
		thread = new GameSaverThread();
		thread.start();

		loader = game.path.getLoader();
		actionsFOS = loader.openFOS(GamePath.ACTIONS, true);
		actionsDOS = new DataOutputStream(actionsFOS);
		//snapshotsWriter = loader.getPrintWriter(GamePath.SNAPSHOTS);
	}

	// всё, кроме strings
	public static void createBaseGame(Game game) throws Exception
	{
		FileLoader loader = game.path.getLoader();
		loader.mkdirs();
		//loader.getFile(GamePath.ACTIONS).createNewFile();
		//loader.getFile(GamePath.SNAPSHOTS).createNewFile();

		if (!game.campaign.isDefault)
			game.campaign.save(loader);
		game.path.addNoteInitial(game);
		game.path.save();
	}
	/*
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
		public void save() throws Exception
		{
			save(game);
		}
		
		public void save(Game game) throws Exception
		{
			//if (campaign != null)
			game.campaign = campaign;

			Scanner scanner = loader.getScanner(GamePath.LAST_SNAPSHOT);
			String lastSnapshot = scanner.nextLine();
			scanner.close();

			PrintWriter writer = loader.getPrintWriter(GamePath.LAST_SNAPSHOT);
			writer.printf("%d %d %s", game.path.numberActions, game.path.numberSnapshots, game.toJson());
			writer.close();

			snapshotsWriter.print(lastSnapshot);
			game.path.numberSnapshots++;
		}
	}
	
	public void saveSnapshot() throws IOException
	{
		add(new SaveSnapshot(mainGame.campaign.createSimpleCopy(game)));
	}
	*/
	
	public class SaveAction implements Save
	{
		public Action action;
		
		public SaveAction(Action action)
		{
			this.action = action;
		}
		
		@Override
		public int save() throws Exception
		{
			action.checkBase(game);
			action.performQuickBase(game);
			//if (!action.isCampaign())
			{
				//action.saveBase(actionsDOS);
				action.toData(actionsDOS);
				++game.path.numberActions;
			}
			return 0;
		}
	}

	public static class SaveWithRC implements Save
	{
		int rc;

		public SaveWithRC(int rc)
		{
			this.rc = rc;
		}

		@Override
		public int save() throws Exception
		{
			return rc;
		}
	}
	
	public void save(Action action) throws IOException
	{
		add(new SaveAction(action));
	}
	
	public void waitSave() throws Exception
	{
		MyAssert.a(thread.isAlive());
		add(new SaveWithRC(1));
		thread.reverseQueue.take();
		actionsDOS.flush();
		//snapshotsWriter.flush();
	}

	public void finishSave() throws Exception
	{
		add(new SaveWithRC(2));
		thread.join();
		actionsDOS.close();
		game.path.save();
		//snapshotsWriter.close();
	}
	
	public void add(Save save)
	{
		thread.queue.add(save);
	}
	
}
