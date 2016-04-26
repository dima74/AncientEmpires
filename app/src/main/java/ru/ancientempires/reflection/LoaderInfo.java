package ru.ancientempires.reflection;

import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;

public class LoaderInfo
{

	public Game  game;
	public Rules rules;

	public LoaderInfo(Game game, Rules rules)
	{
		this.game = game;
		this.rules = rules;
	}

	public LoaderInfo(Game game)
	{
		this(game, game.rules);
	}

}
