package ru.ancientempires.save;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.MyColor;
import ru.ancientempires.SimplePlayer;
import ru.ancientempires.SimpleTeam;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class GameSnapshotSaver
{
	
	private Game		game;
	private FileLoader	loader;
	
	public GameSnapshotSaver(Game game)
	{
		this(game, game.path.getLoader());
	}
	
	public GameSnapshotSaver(Game game, FileLoader loader)
	{
		this.game = game;
		this.loader = loader;
	}
	
	public void save() throws IOException
	{
		game.path.save();
		if (game.path.canChooseTeams)
			saveDefaultTeams();
		else
			saveTeams();
		saveGameInfo();
		savePlayers();
		saveMap();
		saveCells();
		saveUnits();
	}
	
	public void saveGameInfo() throws IOException
	{
		JsonWriter writer = loader.getWriter("gameInfo.json");
		writer.beginObject();
		writer.name("h").value(game.h);
		writer.name("w").value(game.w);
		writer.name("currentPlayer").value(game.currentPlayer.color.name());
		writer.name("currentTurn").value(game.currentTurn);
		writer.endObject();
		writer.close();
	}
	
	public void savePlayers() throws IOException
	{
		JsonWriter writer = loader.getWriter("players.json");
		writer.beginObject();
		writer.name("players").beginArray();
		for (Player player : game.players)
		{
			writer.beginObject();
			writer.name("color").value(player.color.name());
			writer.name("ordinal").value(player.ordinal);
			writer.name("type").value(player.type.name());
			writer.name("gold").value(player.gold);
			writer.name("cursorI").value(player.cursorI);
			writer.name("cursorJ").value(player.cursorJ);
			writer.endObject();
		}
		writer.endArray();
		
		writer.endObject();
		writer.close();
	}
	
	private void saveDefaultTeams() throws IOException
	{
		SimpleTeam[] teams = new SimpleTeam[game.teams.length];
		for (int iTeam = 0; iTeam < teams.length; iTeam++)
		{
			SimplePlayer[] players = new SimplePlayer[game.teams[iTeam].players.length];
			for (int iPlayer = 0; iPlayer < game.teams[iTeam].players.length; iPlayer++)
			{
				Player player = game.teams[iTeam].players[iPlayer];
				players[iPlayer] = new SimplePlayer(player.color, player.ordinal, player.type, player.gold);
			}
			teams[iTeam] = new SimpleTeam(players);
		}
		
		JsonWriter writer = loader.getWriter("defaultTeams.json");
		writer.beginObject();
		writer.name("teams");
		new Gson().toJson(teams, SimpleTeam[].class, writer);
		writer.name("unitsLimit").value(game.unitsLimit);
		writer.endObject();
		writer.close();
	}
	
	private static class SimpleColorsTeam
	{
		public MyColor[] players;
		
		public SimpleColorsTeam(Player[] players)
		{
			this.players = new MyColor[players.length];
			for (int i = 0; i < players.length; i++)
				this.players[i] = players[i].color;
		}
	}
	
	private void saveTeams() throws IOException
	{
		SimpleColorsTeam[] teams = new SimpleColorsTeam[game.teams.length];
		for (int iTeam = 0; iTeam < teams.length; iTeam++)
			teams[iTeam] = new SimpleColorsTeam(game.teams[iTeam].players);
			
		JsonWriter writer = loader.getWriter("teams.json");
		writer.beginObject();
		writer.name("teams");
		new Gson().toJson(teams, SimpleColorsTeam[].class, writer);
		writer.endObject();
		writer.close();
	}
	
	public void saveMap() throws IOException
	{
		DataOutputStream dos = loader.openDOS("map.map");
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
				dos.writeInt(game.fieldCells[i][j].type.ordinal);
		dos.close();
	}
	
	public void saveCells() throws IOException
	{
		JsonWriter writer = loader.getWriter("cells.json");
		writer.beginObject();
		
		writer.name("cells").beginArray();
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
			{
				Cell cell = game.fieldCells[i][j];
				if (cell.type.isStatic)
					continue;
				writer.beginObject();
				
				writer.name("i").value(i);
				writer.name("j").value(j);
				if (cell.isCapture != Cell.defaultCell.isCapture)
				{
					writer.name("isCapture").value(true);
					writer.name("player").value(cell.player.ordinal);
				}
				if (cell.isDestroying)
					writer.name("isDestroying").value(true);
					
				writer.endObject();
			}
		writer.endArray();
		
		writer.endObject();
		writer.close();
	}
	
	public void saveUnits2() throws IOException
	{
		JsonWriter writer = loader.getWriter("units.json");
		writer.beginObject();
		
		writer.name("units").beginArray();
		for (Unit[] line : game.fieldUnits)
			for (Unit unit : line)
				if (unit != null)
					saveUnit(writer, unit);
		for (Unit unit : game.unitsOutside)
			saveUnit(writer, unit);
		writer.endArray();
		
		writer.name("unitsDead").beginArray();
		for (Unit[] line : game.fieldUnitsDead)
			for (Unit unit : line)
				if (unit != null)
					saveUnit(writer, unit);
		writer.endArray();
		
		writer.name("staticUnitsDead").beginArray();
		for (Player player : game.players)
			for (Unit unit : game.unitsStaticDead[player.ordinal])
				saveUnit(writer, unit);
		writer.endArray();
		
		writer.endObject();
		writer.close();
	}
	
	public void saveUnit(JsonWriter writer, Unit unit) throws IOException
	{
		writer.beginObject();
		writer.name("type").value(unit.type.name);
		writer.name("player").value(unit.player.ordinal);
		writer.name("i").value(unit.i);
		writer.name("j").value(unit.j);
		writer.endObject();
	}
	
	public void saveUnits() throws IOException
	{
		DataOutputStream output = loader.openDOS("units.dat");
		saveUnits(output, convertFieldToList(game.fieldUnits));
		saveUnits(output, game.unitsOutside);
		saveUnits(output, convertFieldToList(game.fieldUnitsDead));
		for (ArrayList<Unit> list : game.unitsStaticDead)
			saveUnits(output, list);
		output.close();
	}
	
	private ArrayList<Unit> convertFieldToList(Unit[][] field)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (Unit[] line : field)
			for (Unit unit : line)
				if (unit != null)
					units.add(unit);
		return units;
	}
	
	public void saveUnits(DataOutputStream output, Collection<Unit> units) throws IOException
	{
		output.writeInt(units.size());
		for (Unit unit : units)
			saveUnit(output, unit);
	}
	
	public void saveUnit(DataOutputStream output, Unit unit) throws IOException
	{
		output.writeUTF(unit.type.name);
		output.writeInt(unit.player.ordinal);
		output.writeInt(unit.i);
		output.writeInt(unit.j);
		output.writeInt(unit.health);
		output.writeInt(unit.level);
		output.writeInt(unit.experience);
		output.writeBoolean(unit.isMove);
		output.writeBoolean(unit.isTurn);
		game.namedUnits.trySave(output, unit);
		game.numberedUnits.trySave(output, unit);
	}
	
	public JsonWriter getJsonWriter(File folder, String path) throws IOException
	{
		File file = new File(folder, path);
		file.createNewFile();
		return new JsonWriter(new FileWriter(file));
	}
	
}
