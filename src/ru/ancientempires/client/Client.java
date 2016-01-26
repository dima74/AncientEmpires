package ru.ancientempires.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import android.app.Activity;
import ru.ancientempires.GameInit;
import ru.ancientempires.Localization;
import ru.ancientempires.action.Action;
import ru.ancientempires.framework.LogWriter;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.images.Images;
import ru.ancientempires.images.ImagesLoader;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;
import ru.ancientempires.load.RulesLoader;
import ru.ancientempires.model.Game;
import ru.ancientempires.server.ClientServer;
import ru.ancientempires.server.Server;

public class Client
{
	
	public static Client client;
	
	public static GamePath getGame(String gameID)
	{
		return Client.client.allGames.get(gameID);
	}
	
	public GameInit init;
	
	public LogWriter log;
	
	public FileLoader	fileLoader;
	public FileLoader	gamesLoader;
	public FileLoader	defaultGameLoader;
	public FileLoader	rulesLoader;
	
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
		fileLoader = new FileLoader(activity);
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
	}
	
	// То что нужно для показа списка игр
	public void loadPart1() throws Exception
	{
		gamesLoader.loadLocalization("strings");
		baseFolder = new GamesFolder("", null);
		
		// ID = ANDROID_ID;
		load();
	}
	
	// То что нужно для непосредственно игры
	public void loadPart2() throws Exception
	{
		new RulesLoader(rulesLoader).load();
		images.preload(imagesLoader);
	}
	
	// Начинает загружать 1 и 2 части
	public void startLoadParts12()
	{
		init = new GameInit();
		init.init();
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
	
	public static Game getGame()
	{
		return Client.client.clientServer.game;
	}
	
	public Game startGame(String gameID) throws Exception
	{
		return clientServer.startGame(gameID);
	}
	
	public void stopGame() throws Exception
	{
		clientServer.stopGame();
	}
	
	public static void commit(Action action)
	{
		if (action.changesGame())
		{
			// TODO
			for (Server server : Client.client.servers)
			{
				// TODO
			}
			try
			{
				Client.client.clientServer.commit(action);
			}
			catch (IOException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
		}
	}
	
	public class SaveInfo
	{
		public int numberSaves;
		
	}
	
	public void load() throws Exception
	{
		SaveInfo saveInfo = new Gson().fromJson(fileLoader.getReader("info.json"), SaveInfo.class);
		numberSaves = saveInfo.numberSaves;
	}
	
	public void save() throws Exception
	{
		SaveInfo saveInfo = new SaveInfo();
		saveInfo.numberSaves = numberSaves;
		JsonWriter writer = fileLoader.getWriter("info.json");
		new Gson().toJson(saveInfo, SaveInfo.class, writer);
		writer.close();
	}
	
}
