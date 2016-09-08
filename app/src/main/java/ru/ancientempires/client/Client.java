package ru.ancientempires.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ru.ancientempires.Localization;
import ru.ancientempires.Strings;
import ru.ancientempires.actions.Action;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.Images;
import ru.ancientempires.load.GameInit;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.DefaultRules;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.server.ClientServer;
import ru.ancientempires.server.Server;

public class Client
{
	
	public static Client client;

	public static GamePath getGame(String gameID)
	{
		if (client.init != null)
			try
			{
				client.finishPart1();
			}
			catch (InterruptedException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
		MyAssert.a(Client.client.allGames.containsKey(gameID));
		GamePath game = Client.client.allGames.get(gameID);
		MyAssert.a(game != null);
		return game;
	}
	
	public GameInit init;
	public boolean  part0;

	public FileLoader fileLoader;
	public FileLoader gamesLoader;
	public FileLoader defaultGameLoader;
	public FileLoader savesLoader;

	public          FileLoader rulesLoader;
	volatile public Rules      rules;

	public FileLoader imagesLoader;
	public Images images = new Images();

	public Map<String, GamesFolder> allFolders = new HashMap<>();
	public GamesFolder campaign;
	public GamesFolder skirmish;
	public GamesFolder save;
	public GamesFolder user;
	public Map<String, GamePath> allGames = new HashMap<>();

	public Localization localization = new Localization();

	// public String ID;
	public int numberSaves()
	{
		return save.numberGames;
	}
	
	public String getNameForNewGame()
	{
		return String.format(Strings.EDITOR_GAME_NAME_TEMPLATE.toString(), user.numberGames + 1);
	}
	
	public ClientServer clientServer;
	private Server[] servers = new Server[0];

	public Client(IClientHelper helper) throws Exception
	{
		MyAssert.a(Client.client == null);
		Client.client = this;
		fileLoader = new FileLoader(helper);
		gamesLoader = fileLoader.getLoader("games/");
		rulesLoader = fileLoader.getLoader("rules/");
		defaultGameLoader = gamesLoader.getLoader("defaultGame/");
		imagesLoader = fileLoader.getLoader("images/");
		clientServer = new ClientServer(this);
		// ID = helper.getID();
		savesLoader = gamesLoader.getLoader("save/" + "/");
		loadPart0();
	}
	
	// То что нужно для показа главного меню
	public void loadPart0() throws Exception
	{
		MyLog.l("loadPart0");
		if (part0)
			return;
		part0 = true;
		fileLoader.loadLocalization(this);
		MyLog.l("done loadPart0");
	}
	
	// То что нужно для показа списка игр
	public void loadPart1() throws Exception
	{
		MyLog.l("loadPart1");
		GamesFolder[] folders = new GamesFolder[]
				{
						user = new GamesFolder(this, "user"),
						campaign = new GamesFolder(this, "campaign"),
						skirmish = new GamesFolder(this, "skirmish"),
						save = new GamesFolder(this, "save"),
						new GamesFolder(this, "test")
				};
		for (GamesFolder folder : folders)
			allFolders.put(folder.folderID, folder);
		MyLog.l("done loadPart1");
	}
	
	// То что нужно непосредственно для игры
	public void loadPart2() throws Exception
	{
		MyLog.l("loadPart2");
		//rules = new RulesLoader(rulesLoader).load();
		rules = new DefaultRules().create();
		localization.loadFull(rulesLoader);
		images.setRules(rules);
		images.preload(imagesLoader);
		MyLog.l("done loadPart2");
	}
	
	// Начинает загружать 1 и 2 части
	public void startLoadParts12()
	{
		init = new GameInit();
		init.init(this);
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
		return startGame(gameID, null);
	}

	public Game startGame(String gameID, String players) throws Exception
	{
		return clientServer.startGame(gameID, players);
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
	
}
