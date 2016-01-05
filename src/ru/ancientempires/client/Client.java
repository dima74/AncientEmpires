package ru.ancientempires.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

import android.app.Activity;
import ru.ancientempires.GameInit;
import ru.ancientempires.Localization;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.action.handlers.ActionHandler;
import ru.ancientempires.framework.LogWriter;
import ru.ancientempires.helpers.FileHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.images.Images;
import ru.ancientempires.images.ImagesLoader;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;
import ru.ancientempires.load.RulesLoader;
import ru.ancientempires.model.Game;
import ru.ancientempires.server.ClientServer;
import ru.ancientempires.server.Server;
import ru.ancientempires.tasks.TaskType;
import ru.ancientempires.tasks.handlers.TaskHandler;

public class Client
{
	
	public static Client client;
	
	public static GamePath getGame(String gameID)
	{
		return Client.client.allGames.get(gameID);
	}
	
	public GameInit init;
	
	public LogWriter log;
	
	public FileHelper	fileLoader;
	public FileHelper	gamesLoader;
	public FileHelper	defaultGameLoader;
	public FileHelper	rulesLoader;
	
	public ImagesLoader	imagesLoader;
	public Images		images	= new Images();
	
	public GamesFolder				baseFolder;
	public Map<String, GamesFolder>	allFolders	= new HashMap<String, GamesFolder>();
	public Map<String, GamePath>	allGames	= new HashMap<String, GamePath>();
	
	public Localization localization = new Localization();
	
	public String	ID;
	public int		numberSaves;
	
	public ClientServer	clientServer;
	private Server[]	servers	= new Server[0];
	
	public Client(Activity activity) throws IOException
	{
		log = new LogWriter();
		fileLoader = new FileHelper(activity);
		gamesLoader = fileLoader.getLoader("games/");
		rulesLoader = fileLoader.getLoader("rules/");
		defaultGameLoader = gamesLoader.getLoader("defaultGame/");
		imagesLoader = fileLoader.getLoader("images/").getImagesLoader();
		clientServer = new ClientServer(this);
	}
	
	// То что нужно для показа главного меню
	public void loadPart0() throws IOException
	{
		fileLoader.loadLocalization("strings");
		// Начинает загружать 1 и 2 части
		init = new GameInit();
		init.init();
	}
	
	// То что нужно для показа списка игр
	public void loadPart1() throws IOException
	{
		gamesLoader.loadLocalization("strings");
		baseFolder = new GamesFolder(this, "", null);
		
		// ID = ANDROID_ID;
		JsonReader reader = fileLoader.getReader("info.json");
		reader.beginObject();
		numberSaves = JsonHelper.readInt(reader, "numberSaves");
		reader.endObject();
		reader.close();
	}
	
	// То что нужно для непосредственно игры
	public void loadPart2() throws IOException
	{
		ActionType.init();
		TaskType.init();
		ActionHandler.init();
		TaskHandler.init();
		new RulesLoader(rulesLoader).load();
		images.preload(imagesLoader);
	}
	
	public boolean isFinishPart1()
	{
		return !init.foldersInitThread.isAlive();
	}
	
	public boolean isFinishPart2()
	{
		return !init.initThread.isAlive();
	}
	
	public void finishPart1() throws InterruptedException
	{
		init.foldersInitThread.join();
	}
	
	public void finishPart2() throws InterruptedException
	{
		init.initThread.join();
	}
	
	public Game getGame()
	{
		return clientServer.game;
	}
	
	public void startGame(String gameID) throws IOException
	{
		clientServer.startGame(gameID);
	}
	
	public void stopGame()
	{
		clientServer.stopGame();
	}
	
	public static ActionResult action(Action action)
	{
		if (action.type.critical)
			// TODO
			for (Server server : Client.client.servers)
			{
				// TODO
			}
			
		return Client.client.clientServer.action(action);
	}
	
}
