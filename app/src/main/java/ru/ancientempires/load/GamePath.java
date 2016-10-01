package ru.ancientempires.load;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import ru.ancientempires.actions.Action;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

// Этот класс описывает описывает общую информацию сохранения
public class GamePath
{
	public static final String LAST_SNAPSHOT = "lastSnapshot.json";
	public static final String ACTIONS       = "actions.dat";
	public static final String SNAPSHOTS     = "snapshots.json";
	
	/*
		isBaseGame canChooseTeams
		0 0 каждое сохранение
		0 1 невозможно
		1 0 базовая игра - кампания
		1 1 базовая игра - мультиплеер
	*/

	/*
		Структура папки с игрой:
		.
		├── actions.dat         // непрерывный массив действий
		├── campaign.json
		├── info.json           // содержит координаты центра экрана всех игроков, соответственно обновляется каждый раз при закрытии игры
		├── lastSnapshot.json
		├── snapshots.json      // все снимки, кроме последнего, в каждой строке свой снимок (то есть json объект) + число действий, соответствующих ему
		├── strings.json
		└── strings_ru_RU.json

		Считаем, что часто будут выполняться:
			- загрузка и сохранение игры
			- отмена одного действия
		просмотр истории действий и создание новой игры с начального отрезка текущей --- редкие действия

		Сейчас снимки делаются через равные отрезки, каждые 100 действий.
		Кажется хорошей идея делать снимки по требованию, то есть изначально есть базовый снимок,
		при обычной игре никакие другие снимки сами не создаются, если понадобился снимок игры после первых x действий,
		то он строится на основе ближайшего снизу снимка к x (пусть он представляет y действий),
		при этом если x - y > 2 * c (c ~= 100), то в ходе построения снимка x создаётся снимок, представляющий z действий (y < z < x)
		Я решил брать z = x - c, хотя можно было ещё z = (y + x) / 2

		Итак, основные действия:
		- загрузка игры
			загружаем lastSnapshot.json, в нём написано сколько он действий представляет и сколько байт они занимают в actions.dat,
			применяем действия из actions.dat начиная с этого смещения. При необходимости создаём снимок на ~100 действий ранее полученного
		- сохранение игры
			просто сохраняем все действия, снимки не создаём
		- отмена последнего действия
			загружаем последний из подходящих снимков, применяем действия после него (без одного),
			при необходимости создаём новый снимок на ~100 действий ранее полученного
		- просмотр истории действий
			...
	*/

	// transient для Gson.toJson()
	private transient Client   client;
	public transient  String   path;
	public            String   gameID;
	public            String   baseGameID; // ID игры, в которой хранятся campaign.json & strings.json, может быть равным gameID
	public            String   nextGameID;
	public transient  String   name;
	public            String   defaultLocalization;
	public            String[] localizations;
	public            boolean  isBaseGame;
	public            boolean  canChooseTeams;
	public            int      numberPlayers;
	public            int      h;
	public            int      w;
	public            int      numberSnapshots;
	public            int      numberActions;
	public transient  boolean  isInCampaign;
	public transient int indexActionDisableActiveGame      = -1;
	public transient int sizeBeforeActionDisableActiveGame = -1;
	public           int        sizeActions;
	public transient FileLoader loader;

	public void enterCampaign()
	{
		MyAssert.a(!isInCampaign);
		MyAssert.a(indexActionDisableActiveGame == -1);
		MyAssert.a(sizeBeforeActionDisableActiveGame == -1);
		isInCampaign = true;
		indexActionDisableActiveGame = numberActions;
		sizeBeforeActionDisableActiveGame = sizeActions;
	}

	public void leaveCampaign()
	{
		MyAssert.a(isInCampaign);
		MyAssert.a(indexActionDisableActiveGame >= 0);
		MyAssert.a(sizeBeforeActionDisableActiveGame >= 0);
		isInCampaign = false;
		indexActionDisableActiveGame = -1;
		sizeBeforeActionDisableActiveGame = -1;
	}

	public static class PointScreenCenter
	{
		public float i;
		public float j;

		public PointScreenCenter(int centerI, int centerJ)
		{
			i = centerI + 0.5f;
			j = centerJ + 0.5f;
		}

		public PointScreenCenter(float i, float j)
		{
			this.i = i;
			this.j = j;
		}

		@Override
		public String toString()
		{
			return String.format("{%f, %f}", i, j);
		}
	}

	public PointScreenCenter[] screenCenters;

	// Создаёт новый объект GamePath представляющий собой копию info.json
	public static GamePath get(Client client, String path, boolean addToAllGames) throws Exception
	{
		if (!client.gamesLoader.exists(path + "info.json"))
		{
			MyLog.f("Can't load %s: info.json missing!", path);
			return null;
		}
		GamePath gamePath = new Gson().fromJson(client.gamesLoader.getReader(path + "info.json"), GamePath.class);
		gamePath.client = client;
		gamePath.path = path;
		gamePath.loader = client.gamesLoader.getLoader(path);
		gamePath.loadName();
		if (addToAllGames)
			client.allGames.put(gamePath.gameID, gamePath);
		return gamePath;
	}

	// только для new Gson().fromJson(...)
	public GamePath()
	{}

	// только для AllGamesConverter +оказывается для редактора карт
	public GamePath(Game game, String gameID)
	{
		this.gameID = gameID;
		client = Client.client;
		baseGameID = gameID;
		numberPlayers = game.players.length;
		path = gameID.replace('.', '/') + "/";
		loader = Client.client.gamesLoader.getLoader(path);
		h = game.h;
		w = game.w;
		defaultLocalization = "en_US";
		localizations = new String[]
				{
						"ru_RU"
				};
		screenCenters = new PointScreenCenter[numberPlayers];
		for (int i = 0; i < screenCenters.length; i++)
			screenCenters[i] = new PointScreenCenter(0, 0);
		for (int i = 0; i < game.players.length; i++)
			for (Unit unit : game.players[i].units)
				if (unit.type.isStatic)
				{
					int centerI = unit.i;
					int centerJ = unit.j;
					if (gameID.equals("campaign.4"))
					{
						centerI = 1;
						centerJ = 11;
					}
					screenCenters[i] = new PointScreenCenter(centerI, centerJ);
				}
		game.path = this;
	}

	public Rules getRules()
	{
		return client.rules;
	}

	public void loadName() throws IOException
	{
		FileLoader loader = client.gamesLoader.getLoader(baseGameID.replace('.', '/'));
		if (loader.exists("strings.json"))
			name = client.localization.loadName(loader);
	}

	public FileLoader getLoader()
	{
		return loader;
	}

	public GamePath copyTo(String newPath, String newID) throws IOException
	{
		path = newPath;
		loader = client.gamesLoader.getLoader(path);
		gameID = newID;
		save();
		client.allGames.put(gameID, this);
		getFolder().add(this);
		return this;
	}

	public void save() throws IOException
	{
		if (isInCampaign)
		{
			numberActions = indexActionDisableActiveGame;
			FileOutputStream fos = loader.openFOS(ACTIONS, true);
			fos.getChannel().truncate(sizeBeforeActionDisableActiveGame);
			fos.close();
		}
		JsonWriter writer = getLoader().getWriter("info.json");
		new Gson().toJson(this, GamePath.class, writer);
		writer.close();
	}

	public GamesFolder getFolder()
	{
		return client.allFolders.get(gameID.substring(0, gameID.lastIndexOf('.')));
	}

	public String getFolderID()
	{
		return gameID.substring(0, gameID.lastIndexOf('.'));
	}

	public static class SnapshotNote implements Comparable<SnapshotNote>
	{
		public GamePath path;
		public int      numberActions;
		public int      sizeActions;
		public String   snapshot;

		public SnapshotNote(GamePath path, String name) throws Exception
		{
			this.path = path;
			Scanner scanner = path.getLoader().getScanner(name);
			load(scanner);
			scanner.close();
		}

		public SnapshotNote(GamePath path, Scanner scanner)
		{
			this.path = path;
			load(scanner);
		}

		public SnapshotNote(GamePath path, int numberActions, int sizeActions, Game game) throws Exception
		{
			this.path = path;
			this.numberActions = numberActions;
			this.sizeActions = sizeActions;
			this.snapshot = game.toJson().toString();
		}

		public void load(Scanner scanner)
		{
			numberActions = scanner.nextInt();
			sizeActions = scanner.nextInt();
			snapshot = scanner.nextLine().trim();
		}

		public Game getGame(String players) throws Exception
		{
			JsonObject gameJson = (JsonObject) new JsonParser().parse(snapshot);
			if (players != null)
				gameJson.add("players", new JsonParser().parse(players));
			return new Game(path).fromJson(gameJson);
		}

		// for debug
		public Game getGame(int numberActions) throws Exception
		{
			Game game = getGame(null);
			performActions(game, this, numberActions, true);
			return game;
		}

		public JsonObject getGameJsonDebug() throws Exception
		{
			return (JsonObject) new JsonParser().parse(snapshot);
		}

		@Override
		public int compareTo(SnapshotNote note)
		{
			return Integer.compare(numberActions, note.numberActions);
		}

		@Override
		public String toString()
		{
			return String.format("%d %d %s", numberActions, sizeActions, snapshot);
		}

	}

	public ArrayList<SnapshotNote> getNotes(String name) throws Exception
	{
		ArrayList<SnapshotNote> notes = new ArrayList<>();
		if (loader.exists(name))
		{
			Scanner scanner = loader.getScanner(name);
			while (scanner.hasNext())
				notes.add(new SnapshotNote(this, scanner));
			scanner.close();
		}
		return notes;
	}

	public static Game performActions(Game game, SnapshotNote note, int numberActions, boolean dontAddNote) throws Exception
	{
		GamePath path = note.path;
		MyAssert.a(note.numberActions <= numberActions);
		if (note.numberActions < numberActions)
		{
			FileInputStream fis = path.loader.openFIS(GamePath.ACTIONS);
			fis.getChannel().position(note.sizeActions);
			DataInputStream dis = new DataInputStream(fis);
			ArrayList<Action> actions = new ArrayList<>();
			for (int i = note.numberActions; i < numberActions; i++)
			{
				Action action = game.getLoaderInfo().fromData(dis, Action.class);
				if (!action.checkBase(game))
					path.findBug(numberActions, i);
				action.performQuickBase(game);
				int c = 100;
				if (i == numberActions - c && (numberActions - note.numberActions) > 2 * c && !dontAddNote)
					path.addNote(new SnapshotNote(path, i + 1, (int) fis.getChannel().position(), game));
			}
			//sizeActions = (int) fis.getChannel().position();
			dis.close();
		}
		return game;
	}

	public Action[] getAllActions() throws Exception
	{
		SnapshotNote note = getNoteBefore(0);
		Game game = note.getGame(null);
		FileInputStream fis = loader.openFIS(GamePath.ACTIONS);
		fis.getChannel().position(note.sizeActions);
		DataInputStream dis = new DataInputStream(fis);
		Action[] actions = new Action[numberActions];
		for (int i = 0; i < actions.length; i++)
			actions[i] = game.getLoaderInfo().fromData(dis, Action.class);
		return actions;
	}

	private void findBug(int numberActions, int lastI) throws Exception
	{
		SnapshotNote note0 = getNoteBefore(0);
		SnapshotNote note1 = getNoteBefore(numberActions);

		for (int i = 0; i < lastI; i++)
			MyAssert.a(note0.getGame(i), new Game(this).fromJson(note0.getGame(i).toJson()));

		for (int i = note1.numberActions; i < lastI; i++)
			MyAssert.a(note0.getGame(i), note1.getGame(i));
		System.out.print("");
	}

	public JsonObject getSnapshotJsonDebug() throws Exception
	{
		return getNoteBefore(numberActions).getGameJsonDebug();
	}

	public Game loadGameDebug() throws Exception
	{
		return loadGame(numberActions, false, null, true);
	}

	public Game loadGame(boolean loadCampaign) throws Exception
	{
		return loadGame(loadCampaign, null);
	}

	public Game loadGame(boolean loadCampaign, String players) throws Exception
	{
		return loadGame(numberActions, loadCampaign, players);
	}

	public Game loadGame(int numberActions, boolean loadCampaign, String players) throws Exception
	{
		return loadGame(numberActions, loadCampaign, players, false);
	}

	public Game loadGame(int numberActions, boolean loadCampaign, String players, boolean dontAddNote) throws Exception
	{
		SnapshotNote note = getNoteBefore(numberActions);
		Game game = note.getGame(players);
		if (loadCampaign)
		{
			FileLoader loader = Client.getGame(baseGameID).loader;
			client.defaultGameLoader.loadLocalization(client);
			if (loader.exists("strings.json"))
				loader.loadLocalization(client);

			//game.campaign.load((game.campaign.isDefault = !loader.exists("campaign.json")) ? client.defaultGameLoader : loader);
			game.campaign.isDefault = !loader.exists("campaign.json");
			game.campaign.load(game.campaign.isDefault ? client.defaultGameLoader : loader);
		}
		performActions(game, note, numberActions, !loadCampaign || dontAddNote);
		return game;
	}

	public SnapshotNote getNoteBefore(int numberActions) throws Exception
	{
		SnapshotNote note = new SnapshotNote(this, LAST_SNAPSHOT);
		if (note.numberActions > numberActions)
			for (SnapshotNote noteMaybe : getNotes(SNAPSHOTS))
				if (noteMaybe.numberActions <= numberActions)
					note = noteMaybe;
		return note;
	}

	public void addNote(SnapshotNote newNote) throws Exception
	{
		SnapshotNote lastNote = new SnapshotNote(this, LAST_SNAPSHOT);
		if (lastNote.numberActions < newNote.numberActions)
		{
			PrintWriter writerLastSnapshot = loader.getPrintWriter(LAST_SNAPSHOT);
			writerLastSnapshot.println(newNote);
			writerLastSnapshot.close();

			PrintWriter writerSnapshots = loader.getPrintWriter(SNAPSHOTS, true);
			writerSnapshots.println(lastNote);
			writerSnapshots.close();
		}
		else
		{
			ArrayList<SnapshotNote> notes = getNotes(SNAPSHOTS);
			notes.add(newNote);
			Collections.sort(notes);
			PrintWriter writer = loader.getPrintWriter(SNAPSHOTS);
			for (SnapshotNote note : notes)
				writer.println(note);
			writer.close();
		}
		++numberSnapshots;
	}

	public void addNoteInitial(Game game) throws Exception
	{
		PrintWriter writerLastSnapshot = loader.getPrintWriter(LAST_SNAPSHOT);
		writerLastSnapshot.println(new SnapshotNote(this, 0, 0, game));
		writerLastSnapshot.close();
	}

	@Override
	public String toString()
	{
		return String.format("%s (%s)", gameID, name);
	}

}
