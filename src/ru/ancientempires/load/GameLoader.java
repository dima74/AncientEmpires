package ru.ancientempires.load;

import ru.ancientempires.action.Action;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.save.GameSaveLoader;

public class GameLoader
{
	
	public GamePath	path;
	public Game		game	= new Game();
	public Rules	rules;
	
	public GameLoader(GamePath path, Rules rules)
	{
		this.path = path;
		this.rules = rules;
		game.rules = rules;
	}
	
	public Game load() throws Exception
	{
		game = new GameSnapshotLoader(path, rules).load();
		if (!path.isBaseGame)
		{
			GameSaveLoader loader = path.getGameLoader();
			for (int i = 0; i < loader.saveInfo.numberActionsAfterLastSave; i++)
			{
				Action action = Action.loadNew(loader.actions().openDIS("" + i));
				action.checkBase(game);
				action.performQuickBase(game);
			}
		}
		return game;
	}
	
}
