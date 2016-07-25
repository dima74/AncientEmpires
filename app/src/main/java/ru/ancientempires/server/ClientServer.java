package ru.ancientempires.server;

import java.io.IOException;

import ru.ancientempires.actions.Action;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GameSaver;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;

public class ClientServer extends Server
{
	
	public Client client;
	public Game   game;

	public ClientServer(Client client)
	{
		this.client = client;
	}

	public Game startGame(String gameID) throws Exception
	{
		return startGame(gameID, null);
	}

	public Game startGame(String gameID, String players) throws Exception
	{
		MyLog.l("");
		MyLog.l("");
		MyLog.l("===================================================".substring(0, ("startGame " + gameID).length()));
		MyLog.l("startGame " + gameID);

		// Если это не базовая игра, то просто загружаем её
		// Иначе копируем в games/ANDROID_ID/
		GamePath path = Client.getGame(gameID);
		Rules rules = path.getRules();
		game = path.loadGame(true, players);
		if (path.isBaseGame)
		{
			String newID = "save." + client.numberSaves();
			String newPath = newID.replace('.', '/') + "/";
			client.gamesLoader.getLoader(newPath).mkdirs();
			
			GamePath newGamePath = GamePath
					.get(client, path.path, false)
					.copyTo(newPath, newID);
			newGamePath.isBaseGame = false;
			newGamePath.canChooseTeams = false;
			game.path = newGamePath;
		}
		client.images.load(client.imagesLoader, game);
		game.saver = new GameSaver(game, path.isBaseGame);
		game.isMain = true;
		game.ii.rules = rules;
		return game;
	}

	@Override
	public void stopGame() throws Exception
	{
		game.saver.finishSave();
	}
	
	public void commit(Action action) throws IOException
	{
		// action.saveBase(MyAssert.output);
		if (MyAssert.outputText != null)
			MyAssert.outputText.println(action);
		try
		{
			// Вот здесь у него поменялось значение поля Game!
			game.saver.save(action);
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
}
