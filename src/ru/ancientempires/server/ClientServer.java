package ru.ancientempires.server;

import java.io.IOException;

import ru.ancientempires.action.Action;
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
			String newID = "save." + client.numberSaves();
			String newPath = newID.replace('.', '/') + "/";
			client.gamesLoader.getLoader(newPath).mkdirs();
			
			GamePath newGamePath = GamePath
					.get(path.path, false)
					.copyTo(newPath, newID);
			newGamePath.isBaseGame = false;
			newGamePath.canChooseTeams = false;
			game = new GameLoader(path, rules).load(true);
			game.path = newGamePath;
			// game.random = new Random(274755610533487L);
			
			game.path.getFolder().add(game.path);
			
			game.saver = new GameSaver(game);
			game.saver.initFromBase();
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
	}
	
	public void commit(Action action) throws IOException
	{
		// action.saveBase(MyAssert.output);
		if (MyAssert.outputText != null)
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
