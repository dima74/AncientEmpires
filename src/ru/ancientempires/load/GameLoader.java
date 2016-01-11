package ru.ancientempires.load;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import ru.ancientempires.MyColor;
import ru.ancientempires.PlayerType;
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
import ru.ancientempires.model.UnitType;

public class GameLoader
{
	
	public GamePath		path;
	public FileLoader	loader;
	public Game			game	= new Game();;
	
	public GameLoader(GamePath path)
	{
		this.path = path;
		loader = path.getLoader();
	}
	
	public Game loadGame() throws IOException
	{
		game.path = path;
		loadPlayers();
		loadGameInfo();
		if (loader.exists("teams.json"))
			loadTeams();
		else
			loadLastTeams();
		loadMap();
		loadCells();
		loadUnits();
		
		Client.client.defaultGameLoader.loadLocalization("strings");
		if (loader.exists("strings.json"))
			loader.loadLocalization("strings");
			
		// if (false)
		try
		{
			if (loader.exists("campaign.json"))
				game.campaign.load(loader, game);
			else
				game.campaign.load(Client.client.defaultGameLoader, game);
		}
		catch (Exception e)
		{
			MyAssert.a(false);
		}
		
		//
		for (CellType cellType : CellType.types)
		{
			cellType.buyUnits = new ArrayList[game.players.length];
			for (int iPlayer = 0; iPlayer < game.players.length; iPlayer++)
			{
				cellType.buyUnits[iPlayer] = new ArrayList<Unit>();
				for (UnitType unitType : cellType.buyUnitsDefault)
					cellType.buyUnits[iPlayer].add(new Unit(unitType, game.players[iPlayer]));
			}
		}
		
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
		reader.endObject();
		reader.close();
	}
	
	private void loadPlayers() throws IOException
	{
		JsonReader reader = loader.getReader("players.json");
		reader.beginObject();
		MyAssert.a("players", reader.nextName());
		
		reader.beginArray();
		ArrayList<Player> players = new ArrayList<Player>();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			reader.beginObject();
			Player player = new Player();
			player.color = MyColor.valueOf(JsonHelper.readString(reader, "color"));
			player.ordinal = JsonHelper.readInt(reader, "ordinal");
			player.type = PlayerType.valueOf(JsonHelper.readString(reader, "type"));
			player.gold = JsonHelper.readInt(reader, "gold");
			player.cursorI = JsonHelper.readInt(reader, "cursorI");
			player.cursorJ = JsonHelper.readInt(reader, "cursorJ");
			players.add(player);
			reader.endObject();
		}
		reader.endArray();
		reader.endObject();
		reader.close();
		
		game.players = new Player[players.size()];
		for (Player player : players)
			game.players[player.ordinal] = player;
	}
	
	private static class SimpleColorsTeam
	{
		public MyColor[] players;
	}
	
	private void loadTeams() throws IOException
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
	
	private void loadLastTeams() throws IOException
	{
		JsonReader reader = loader.getReader("lastTeams.json");
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
	
	private void loadMap() throws NumberFormatException, IOException
	{
		DataInputStream input = loader.openDIS("map.map");
		game.fieldCells = new Cell[game.h][game.w];
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
			{
				int ordinal = input.readInt();
				CellType type = CellType.getType(ordinal);
				game.fieldCells[i][j] = Cell.getNew(type);
			}
		input.close();
	}
	
	private void loadCells() throws IOException
	{
		JsonReader reader = loader.getReader("cells.json");
		reader.beginObject();
		MyAssert.a("cells", reader.nextName());
		
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			Cell cell = RulesLoader.nextCell(reader, game.players);
			CellType type = game.fieldCells[cell.i][cell.j].type;
			cell.type = type;
			game.fieldCells[cell.i][cell.j] = cell;
		}
		reader.endArray();
		
		reader.endObject();
		reader.close();
	}
	
	private void loadAllUnits() throws IOException
	{
		JsonReader reader = loader.getReader("units.json");
		game.unitsOutside = new HashSet<Unit>();
		
		reader.beginObject();
		game.fieldUnits = loadUnits(reader, "units", true);
		game.fieldUnitsDead = loadUnits(reader, "unitsDead", false);
		loadstaticUnitsDead(reader);
		reader.endObject();
		reader.close();
	}
	
	private Unit[][] loadUnits(JsonReader reader, String jsonName, boolean isUnitsLive) throws IOException
	{
		Unit[][] fieldUnits = new Unit[game.h][game.w];
		
		MyAssert.a(jsonName, reader.nextName());
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			Unit unit = nextUnit(reader);
			if (isUnitsLive)
				unit.player.units.add(unit);
			if (0 <= unit.i && unit.i < game.h && 0 <= unit.j && unit.j < game.w)
				fieldUnits[unit.i][unit.j] = unit;
			else if (isUnitsLive)
				game.unitsOutside.add(unit);
		}
		reader.endArray();
		
		return fieldUnits;
	}
	
	private void loadstaticUnitsDead(JsonReader reader) throws IOException
	{
		game.unitsStaticDead = new ArrayList[game.players.length];
		for (int i = 0; i < game.players.length; i++)
			game.unitsStaticDead[i] = new ArrayList<Unit>();
			
		MyAssert.a("staticUnitsDead", reader.nextName());
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
			game.unitsStaticDead[nextUnit(reader).player.ordinal].add(nextUnit(reader));
		reader.endArray();
	}
	
	private Unit nextUnit(JsonReader reader) throws IOException
	{
		reader.beginObject();
		Unit unit = new Unit(UnitType.getType(JsonHelper.readString(reader, "type")), game.players[JsonHelper.readInt(reader, "player")]);
		RulesLoader.loadUnitPropetries(reader, unit);
		reader.endObject();
		return unit;
	}
	
	private void loadUnits() throws IOException
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
	
	private Unit[][] loadUnitsField(DataInputStream input, boolean addToPlayer) throws IOException
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		loadUnits(input, units, addToPlayer);
		Unit[][] field = new Unit[game.h][game.w];
		for (Unit unit : units)
			field[unit.i][unit.j] = unit;
		return field;
	}
	
	public void loadUnits(DataInputStream input, Collection<Unit> units, boolean addToPlayer) throws IOException
	{
		int numberUnits = input.readInt();
		for (int i = 0; i < numberUnits; i++)
			units.add(loadUnit(input, addToPlayer));
	}
	
	private Unit loadUnit(DataInputStream input, boolean addToPlayer) throws IOException
	{
		UnitType type = UnitType.getType(input.readUTF());
		Player player = game.players[input.readInt()];
		Unit unit = new Unit(type, player);
		unit.i = input.readInt();
		unit.j = input.readInt();
		unit.health = input.readInt();
		unit.level = input.readInt();
		unit.experience = input.readInt();
		unit.isMove = input.readBoolean();
		unit.isTurn = input.readBoolean();
		game.namedUnits.tryLoad(input, unit);
		game.numberedUnits.tryLoad(input, unit);
		
		if (addToPlayer)
			player.units.add(unit);
		return unit;
	}
	
}
