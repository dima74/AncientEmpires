package ru.ancientempires.server;

import java.io.IOException;

import ru.ancientempires.action.Action;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GameLoader;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.save.GameSaver;

public class ClientServer extends Server
{
	
	public Client	client;
	public Game		game;
	
	public ClientServer(Client client)
	{
		this.client = client;
	}
	
	@Override
	public Game startGame(String gameID) throws Exception
	{
		// Если это не базовая игра, то просто загружаем её
		// Иначе копируем в games/ANDROID_ID/
		GamePath path = Client.getGame(gameID);
		Rules rules = path.getRules();
		if (path.isBaseGame)
		{
			String newID = client.ID + ".save" + client.numberSaves++;
			String newPath = newID.replace('.', '/') + "/";
			client.gamesLoader.getLoader(newPath).mkdirs();
			
			GamePath newGamePath = GamePath
					.get(path.path, false)
					.copyTo(newPath, newID);
			newGamePath.isBaseGame = false;
			newGamePath.canChooseTeams = false;
			game = new GameLoader(path, rules).load(true);
			game.path = newGamePath;
			// game.random = new Random(213237048392331L);
			
			game.saver = new GameSaver(game);
			game.saver.initFromBase();
			
			client.save();
		}
		else
		{
			game = new GameLoader(path, rules).load(true);
			game.path = path;
			game.saver = new GameSaver(game);
			game.saver.init();
		}
		client.images.load(client.imagesLoader, game);
		
		game.isMain = true;
		game.ii.rules = rules;
		return game;
	}
	
	@Override
	public void stopGame() throws Exception
	{
		game.saver.finishSave();
		if (game.path.nextGameID != null)
			GameActivity.startGame(game.path.nextGameID, false);
	}
	
	public void commit(Action action) throws IOException
	{
		// action.saveBase(MyAssert.output);
		MyAssert.outputText.println(action);
		try
		{
			// Вот здесь у него поменялось знасение поля Game!
			game.saver.save(action);
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
}
