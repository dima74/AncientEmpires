package ru.ancientempires.load;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import ru.ancientempires.MyColor;
import ru.ancientempires.SimpleTeam;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Team;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.ReflectionLoader;
import ru.ancientempires.tasks.Task;

public class GameSnapshotLoader
{
	
	public GamePath   path;
	public FileLoader loader;
	public FileLoader loaderCampaign;
	public Game       game;
	public Rules      rules;
	public LoaderInfo info;

	public GameSnapshotLoader(GamePath path, Rules rules) throws Exception
	{
		this.path = path;
		this.rules = rules;
		game = new Game(rules);
		loaderCampaign = path.getLoader();
		//loader = path.getGameSaveLoader().snapshots();
		info = new LoaderInfo(game);
	}
	
	public Game load(boolean loadCampaign) throws Exception
	{
		loadPlayers();
		loadGameInfo();
		if (loader.exists("teams.json"))
			loadTeams();
		else
			// нету ни teams.json ни lastTeams.json, есть только defaultTeams.json
			loadLastTeams();
		loadMap();
		loadCells();
		loadUnits();
		loadTasks();
		loadNamedPoints();
		loadNamedBooleans();
		
		if (loadCampaign)
		{
			Client.client.defaultGameLoader.loadLocalization();
			if (loaderCampaign.exists("strings.json"))
				loaderCampaign.loadLocalization();

			boolean defaultCampaign = !loaderCampaign.exists("campaign.json");
			if (defaultCampaign)
			{
				game.campaign.load(Client.client.defaultGameLoader);
				game.campaign.isDefault = true;
			}
			else
				game.campaign.load(loaderCampaign);

			if (path.isBaseGame && game.campaign.isDefault)
				game.campaign.loadState(Client.client.defaultGameLoader);
			else
				game.campaign.loadState(loader);
		}
		
		//
		for (CellType cellType : rules.cellTypes)
		{
			cellType.buyUnits = new Unit[game.players.length][];
			for (int iPlayer = 0; iPlayer < game.players.length; iPlayer++)
			{
				cellType.buyUnits[iPlayer] = new Unit[cellType.buyTypes.length];
				for (int i = 0; i < cellType.buyTypes.length; i++)
					cellType.buyUnits[iPlayer][i] = new Unit(game, cellType.buyTypes[i], game.players[iPlayer]);
			}
		}
		
		//
		game.currentEarns = new int[game.players.length];
		for (Cell[] line : game.fieldCells)
			for (Cell cell : line)
				if (cell.player != null)
					game.currentEarns[cell.player.ordinal] += cell.type.earn;

		game.path = path;
		return game;
	}

	public void loadNamedPoints() throws Exception
	{
		JsonReader reader = loader.getReader("namedPoints.json");
		reader.beginObject();
		while (reader.peek() != JsonToken.END_OBJECT)
			game.namedPoints.objects.put(reader.nextName(), ReflectionLoader.load(reader, AbstractPoint.class, new LoaderInfo(game)));
		reader.endObject();
		reader.close();
	}

	public void loadNamedBooleans() throws Exception
	{
		JsonReader reader = loader.getReader("namedBooleans.json");
		reader.beginObject();
		while (reader.peek() != JsonToken.END_OBJECT)
			game.namedBooleans.objects.put(reader.nextName(), reader.nextBoolean());
		reader.endObject();
		reader.close();
	}

	public void loadGameInfo() throws IOException
	{
		JsonReader reader = loader.getReader("gameInfo.json");
		reader.beginObject();
		game.h = JsonHelper.readInt(reader, "h");
		game.w = JsonHelper.readInt(reader, "w");
		game.currentPlayer = game.getPlayer(MyColor.valueOf(JsonHelper.readString(reader, "currentPlayer")));
		game.currentTurn = JsonHelper.readInt(reader, "currentTurn");
		game.unitsLimit = JsonHelper.readInt(reader, "unitsLimit");
		game.allowedUnits = JsonHelper.readInt(reader, "allowedUnits");
		long seed = JsonHelper.readLong(reader, "seed");
		game.random = new Random(seed);
		if (path.isBaseGame)
			game.random = new Random();
		reader.endObject();
		reader.close();
	}
	
	public void loadPlayers() throws IOException
	{
		JsonReader reader = loader.getReader("players.json");
		JsonArray players = new JsonParser().parse(reader).getAsJsonArray();
		reader.close();
		
		game.players = new Player[players.size()];
		for (int i = 0; i < players.size(); i++)
		{
			//Player player = new Player(players.get(i).getAsJsonObject());
			//game.players[player.ordinal] = player;
		}
	}
	
	public static class SimpleColorsTeam
	{
		MyColor[] players;
	}
	
	public void loadTeams() throws IOException
	{
		JsonReader reader = loader.getReader("teams.json");
		reader.beginObject();
		MyAssert.a("teams", reader.nextName());
		SimpleColorsTeam[] teams = new Gson().fromJson(reader, SimpleColorsTeam[].class);
		reader.endObject();
		reader.close();
		
		game.teams = new Team[teams.length];
		for (int i = 0; i < teams.length; i++)
		{
			Player[] players = new Player[teams[i].players.length];
			for (int j = 0; j < players.length; j++)
				players[j] = game.getPlayer(teams[i].players[j]);
			//game.teams[i] = new Team(i, players);
			MyAssert.a(false);
		}
	}
	
	public void loadLastTeams() throws IOException
	{
		
		JsonReader reader = path.getLoader().exists("defaultTeams.json")
				? path.getLoader().getReader("defaultTeams.json")
				: path.getLoader().getReader("lastTeams.json");
		reader.beginObject();
		MyAssert.a("teams", reader.nextName());
		SimpleTeam[] teams = new Gson().fromJson(reader, SimpleTeam[].class);
		game.teams = new Team[teams.length];
		for (int i = 0; i < teams.length; i++)
		{
			Player[] players = new Player[teams[i].players.length];
			for (int j = 0; j < players.length; j++)
			{
				players[j] = game.getPlayer(teams[i].players[j].color);
				players[j].type = teams[i].players[j].type;
				players[j].gold = teams[i].players[j].gold;
			}
			//game.teams[i] = new Team(i, players);
			MyAssert.a(false);
		}
		game.unitsLimit = JsonHelper.readInt(reader, "unitsLimit");
		reader.endObject();
		reader.close();
		
		Player[] players = new Player[game.players.length];
		for (Player player : game.players)
			players[player.ordinal] = player;
		game.players = players;
	}
	
	public void loadMap() throws NumberFormatException, IOException
	{
		DataInputStream input = loader.openDIS("map.dat");
		game.fieldCells = new Cell[game.h][game.w];
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
			{
				int ordinal = input.readInt();
				CellType type = rules.cellTypes[ordinal];
				game.fieldCells[i][j] = new Cell(game, type, i, j);
			}
		input.close();
	}
	
	public void loadCells() throws Exception
	{
		DataInputStream input = loader.openDIS("cells.dat");
		int numberCells = input.readInt();
		for (int iCell = 0; iCell < numberCells; iCell++)
		{
			int i = input.readShort();
			int j = input.readShort();
			game.fieldCells[i][j].load(input, info);
		}
		input.close();
		
		for (Cell[] line : game.fieldCells)
			for (Cell cell : line)
				if (cell.type.template != null)
					cell.type.template.update(cell);
	}
	
	public void loadUnits() throws Exception
	{
		DataInputStream input = loader.openDIS("units.dat");
		game.fieldUnits = loadUnitsField(input, true);
		loadUnits(input, game.unitsOutside = new HashSet<Unit>(), true);
		game.fieldUnitsDead = loadUnitsField(input, false);
		
		game.unitsStaticDead = new ArrayList[game.players.length];
		for (int i = 0; i < game.players.length; i++)
			loadUnits(input, game.unitsStaticDead[i] = new ArrayList<>(), false);
		input.close();
	}
	
	public Unit[][] loadUnitsField(DataInputStream input, boolean addToPlayer) throws Exception
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		loadUnits(input, units, addToPlayer);
		Unit[][] field = new Unit[game.h][game.w];
		for (Unit unit : units)
			field[unit.i][unit.j] = unit;
		return field;
	}
	
	public void loadUnits(DataInputStream input, Collection<Unit> units, boolean addToPlayer) throws Exception
	{
		int numberUnits = input.readInt();
		for (int i = 0; i < numberUnits; i++)
			units.add(loadUnit(input, addToPlayer));
	}
	
	public Unit loadUnit(DataInputStream input, boolean addToPlayer) throws Exception
	{
		Unit unit = new Unit(game);
		unit.load(input, game);
		if (addToPlayer)
			unit.player.units.add(unit);
		return unit;
	}
	
	public void loadTasks() throws Exception
	{
		DataInputStream input = loader.openDIS("tasks.dat");
		int number = input.readInt();
		for (int i = 0; i < number; i++)
		{
			int turn = input.readInt();
			int numberTasks = input.readInt();
			ArrayList<Task> tasks = new ArrayList<>(numberTasks);
			for (int j = 0; j < numberTasks; j++)
			{
				Task task = Task.loadNew(input, game);
				tasks.add(task);
			}
			game.tasks.put(turn, tasks);
		}
		input.close();
	}
	
}
