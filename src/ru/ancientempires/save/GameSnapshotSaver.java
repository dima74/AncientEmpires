package ru.ancientempires.save;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.MyColor;
import ru.ancientempires.SimplePlayer;
import ru.ancientempires.SimpleTeam;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.tasks.Task;

public class GameSnapshotSaver
{
	
	public Game			game;
	public FileLoader	loader;
						
	public GameSnapshotSaver(Game game, FileLoader loader)
	{
		this.game = game;
		this.loader = loader;
	}
	
	public void save() throws IOException
	{
		if (game.path.canChooseTeams)
			saveDefaultTeams();
		else
			saveTeams();
		saveTasks();
		saveGameInfo();
		savePlayers();
		saveMap();
		saveCells();
		saveUnits();
		if (!game.path.isBaseGame || !game.campaign.isDefault)
			game.campaign.saveState(loader);
	}
	
	public void saveGameInfo() throws IOException
	{
		JsonWriter writer = loader.getWriter("gameInfo.json");
		writer.beginObject();
		writer.name("h").value(game.h);
		writer.name("w").value(game.w);
		writer.name("currentPlayer").value(game.currentPlayer.color.name());
		writer.name("currentTurn").value(game.currentTurn);
		writer.name("unitsLimit").value(game.unitsLimit);
		writer.name("seed").value(game.getSeed());
		writer.endObject();
		writer.close();
	}
	
	public void savePlayers() throws IOException
	{
		JsonArray players = new JsonArray();
		for (Player player : game.players)
			players.add(player.json());
			
		JsonWriter writer = loader.getWriter("players.json");
		new Gson().toJson(players, writer);
		writer.close();
	}
	
	public void saveDefaultTeams() throws IOException
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
		
		JsonWriter writer = game.path.getLoader().getWriter("defaultTeams.json");
		writer.beginObject();
		writer.name("teams");
		new Gson().toJson(teams, SimpleTeam[].class, writer);
		writer.name("unitsLimit").value(game.unitsLimit);
		writer.endObject();
		writer.close();
	}
	
	public static class SimpleColorsTeam
	{
		public MyColor[] players;
		
		public SimpleColorsTeam(Player[] players)
		{
			this.players = new MyColor[players.length];
			for (int i = 0; i < players.length; i++)
				this.players[i] = players[i].color;
		}
	}
	
	public void saveTeams() throws IOException
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
		DataOutputStream output = loader.openDOS("map.dat");
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
				output.writeInt(game.fieldCells[i][j].type.ordinal);
		output.close();
	}
	
	public void saveCells() throws IOException
	{
		DataOutputStream output = loader.openDOS("cells.dat");
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (Cell[] line : game.fieldCells)
			for (Cell cell : line)
				if (cell.needSave())
					cells.add(cell);
		output.writeInt(cells.size());
		for (Cell cell : cells)
		{
			output.writeShort(cell.i);
			output.writeShort(cell.j);
			cell.save(output, game);
		}
		output.close();
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
	
	public ArrayList<Unit> convertFieldToList(Unit[][] field)
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
			unit.save(output, game);
	}
	
	public void saveTasks() throws IOException
	{
		DataOutputStream output = loader.openDOS("tasks.dat");
		int number = 0;
		for (Entry<Integer, ArrayList<Task>> entry : game.tasks.entrySet())
			if (!entry.getValue().isEmpty())
				number++;
		output.writeInt(number);
		for (Entry<Integer, ArrayList<Task>> entry : game.tasks.entrySet())
			if (!entry.getValue().isEmpty())
			{
				output.writeInt(entry.getKey());
				output.writeInt(entry.getValue().size());
				for (Task task : entry.getValue())
					task.saveBase(output);
			}
		output.close();
	}
	
}
