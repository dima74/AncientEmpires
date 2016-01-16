package ru.ancientempires.load;

import ru.ancientempires.action.Action;
import ru.ancientempires.model.Game;
import ru.ancientempires.save.GameSaveLoader;

public class GameLoader
{
	
	public GamePath	path;
	public Game		game	= new Game();
	
	public GameLoader(GamePath path)
	{
		this.path = path;
	}
	
	public Game load() throws Exception
	{
		game = new GameSnapshotLoader(path).load();
		if (!path.isBaseGame)
		{
			GameSaveLoader loader = path.getGameLoader();
			for (int i = 0; i < loader.saveInfo.numberActionsAfterLastSave; i++)
			{
				Action action = Action.loadNew(loader.actions().openDIS("" + i));
				action.game = game;
				action.perform();
			}
		}
		return game;
	}
	
}
