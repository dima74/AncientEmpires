package ru.ancientempires.load;

import ru.ancientempires.action.Action;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.save.GameSaveLoader;

public class GameLoader
{
	
	public GamePath	path;
	public Game		game;
	public Rules	rules;
	
	public GameLoader(GamePath path, Rules rules)
	{
		this.path = path;
		this.rules = rules;
		game = new Game(rules);
	}
	
	public Game load(boolean loadCampaign) throws Exception
	{
		game = new GameSnapshotLoader(path, rules).load(loadCampaign);
		if (!path.isBaseGame)
		{
			GameSaveLoader loader = path.getGameSaveLoader();
			for (int i = 0; i < loader.numberActionsAfterLastSave; i++)
			{
				Action action = Action.loadNew(loader.actions().openDIS("" + i));
				action.checkBase(game);
				action.performQuickBase(game);
			}
		}
		return game;
	}
	
}
