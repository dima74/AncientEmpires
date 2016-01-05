package ru.ancientempires.server;

import java.io.IOException;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.handlers.ActionHandler;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GameLoader;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.model.Game;
import ru.ancientempires.save.GameSaver;

public class ClientServer extends Server
{
	
	private Client	client;
	public Game		game;
	
	public ClientServer(Client client)
	{
		this.client = client;
	}
	
	@Override
	public void startGame(String gameID) throws IOException
	{
		// Если это не базовая игра, то просто загружаем её
		// Иначе копируем в games/ANDROID_ID/
		try
		{
			GamePath path = Client.getGame(gameID);
			if (path.isBaseGame)
			{
				String newID = client.ID + ".save" + client.numberSaves++ + "/";
				String newPath = newID.replace('.', '/');
				GamePath newGamePath = new GamePath(client, path.path, newID);
				newGamePath.path = newPath;
				newGamePath.isBaseGame = false;
				newGamePath.canChooseTeams = false;
				game = new GameLoader(path).loadGame();
				game.path = newGamePath;
			}
			else
				game = new GameLoader(path).loadGame();
			client.images.load(client.imagesLoader, game);
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		game.saver = new GameSaver(game);
		game.saver.save();
		
		GameHandler.initGame(game);
	}
	
	@Override
	public void stopGame()
	{
		if (game.path.nextGameID != null)
			GameActivity.startGame(game.path.nextGameID, false);
	}
	
	public ActionResult action(Action action)
	{
		ActionHandler actionHandler = ActionHandler.getActionHandler(action.type);
		// MyLog.l(actionHandler.getClass().getSimpleName().replace("ActionHandler", "") + " " + action);
		ActionResult result = actionHandler.action(action);
		MyAssert.a(result.successfully);
		if (!result.successfully)
			actionHandler.action(action);
			
		try
		{
			game.saver.save(action);
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		
		return result;
	}
	
}
