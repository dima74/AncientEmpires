package ru.ancientempires.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.rules.RulesLoader;
import ru.ancientempires.server.ClientServer;
import ru.ancientempires.server.Server;

public class Client
{
	
	public static Client client;
	
	public static GamePath getGame(String gameID)
	{
		return Client.client.allGames.get(gameID);
	}
	
	public GameInit					init;
									
	public LogWriter				log;
									
	public FileLoader				fileLoader;
	public FileLoader				gamesLoader;
	public FileLoader				defaultGameLoader;
	public FileLoader				savesLoader;
									
	public FileLoader				rulesLoader;
	public Rules					rules;
									
	public ImagesLoader				imagesLoader;
	public Images					images			= new Images();
													
	public Map<String, GamesFolder>	allFolders		= new HashMap<String, GamesFolder>();
	public GamesFolder				campaign;
	public GamesFolder				skirmish;
	public GamesFolder				save;
	public Map<String, GamePath>	allGames		= new HashMap<String, GamePath>();
													
	public Localization				localization	= new Localization();
													
	// public String ID;
	public int numberSaves()
	{
		return save.numberGames;
	}
	
	public ClientServer	clientServer;
	private Server[]	servers	= new Server[0];
								
	public Client(IClientHelper helper) throws IOException
	{
		MyAssert.a(Client.client == null);
		Client.client = this;
		log = new LogWriter();
		fileLoader = new FileLoader(helper);
		gamesLoader = fileLoader.getLoader("games/");
		rulesLoader = fileLoader.getLoader("rules/");
		defaultGameLoader = gamesLoader.getLoader("defaultGame/");
		imagesLoader = fileLoader.getLoader("images/").getImagesLoader();
		clientServer = new ClientServer(this);
		// ID = helper.getID();
		savesLoader = gamesLoader.getLoader("save/" + "/");
		
		// loadPart0();
	}
	
	// То что нужно для показа главного меню
	private void loadPart0() throws IOException
	{
		fileLoader.loadLocalization();
	}
	
	// То что нужно для показа списка игр
	public void loadPart1() throws Exception
	{
		GamesFolder[] folders = new GamesFolder[]
		{
				campaign = new GamesFolder("campaign"),
				skirmish = new GamesFolder("skirmish"),
				save = new GamesFolder("save"),
				new GamesFolder("test")
		};
		for (GamesFolder folder : folders)
			allFolders.put(folder.folderID, folder);
	}
	
	// То что нужно для непосредственно игры
	public void loadPart2() throws Exception
	{
		rules = new RulesLoader(rulesLoader).load();
		images.setRules(rules);
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
	
	public void stopGame(boolean startNext) throws Exception
	{
		clientServer.stopGame(startNext);
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
	
}
