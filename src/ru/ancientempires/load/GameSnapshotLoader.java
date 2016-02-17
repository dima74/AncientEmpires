package ru.ancientempires.load;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import ru.ancientempires.MyColor;
import ru.ancientempires.SimpleTeam;
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
import ru.ancientempires.tasks.Task;

class GameSnapshotLoader
{
	
	public GamePath		path;
	public FileLoader	loader;
	public FileLoader	loaderCampaign;
	public Game			game;
	public Rules		rules;
	
	public GameSnapshotLoader(GamePath path, Rules rules) throws Exception
	{
		this.path = path;
		this.rules = rules;
		game = new Game(rules);
		loaderCampaign = path.getLoader();
		loader = path.getGameSaveLoader().snapshots();
	}
	
	public Game load(boolean loadCampaign) throws Exception
	{
		loadPlayers();
		loadGameInfo();
		if (loader.exists("teams.json"))
			loadTeams();
		else
			loadLastTeams();
		loadMap();
		loadCells();
		loadUnits();
		loadTasks();
		
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
					cellType.buyUnits[iPlayer][i] = new Unit(cellType.buyTypes[i], game.players[iPlayer], game);
			}
		}
		
		//
		game.currentEarns = new int[game.players.length];
		for (Cell[] line : game.fieldCells)
			for (Cell cell : line)
				if (cell.player != null)
					game.currentEarns[cell.player.ordinal] += cell.type.earn;
					
		return game;
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
			Player player = new Player(players.get(i).getAsJsonObject());
			game.players[player.ordinal] = player;
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
			game.teams[i] = new Team(players);
		}
	}
	
	public void loadLastTeams() throws IOException
	{
		JsonReader reader = path.getLoader().getReader("lastTeams.json");
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
			game.teams[i] = new Team(players);
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
				game.fieldCells[i][j] = new Cell(type, i, j);
			}
		input.close();
	}
	
	public void loadCells() throws IOException
	{
		DataInputStream input = loader.openDIS("cells.dat");
		int numberCells = input.readInt();
		for (int iCell = 0; iCell < numberCells; iCell++)
		{
			int i = input.readShort();
			int j = input.readShort();
			game.fieldCells[i][j].load(input, game);
		}
		input.close();
	}
	
	public void loadUnits() throws Exception
	{
		DataInputStream input = loader.openDIS("units.dat");
		game.fieldUnits = loadUnitsField(input, true);
		loadUnits(input, game.unitsOutside = new HashSet<Unit>(), true);
		game.fieldUnitsDead = loadUnitsField(input, false);
		
		game.unitsStaticDead = new ArrayList[game.players.length];
		for (int i = 0; i < game.players.length; i++)
			loadUnits(input, game.unitsStaticDead[i] = new ArrayList<Unit>(), false);
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
			ArrayList<Task> tasks = new ArrayList<Task>(numberTasks);
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
