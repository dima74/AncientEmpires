package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.struct.StructInfo;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.MyNullable;
import ru.ancientempires.serializable.SerializableJson;

public class Cell extends AbstractGameHandler implements SerializableJson
{

	@Exclude
	public CellType type;
	public int      i;
	public int      j;

	// только для захватываемых клеточек
	@MyNullable
	public Player player;

	public boolean isCapture()
	{
		return player != null;
	}

	@Exclude
	public int        specialization;
	@MyNullable
	public StructInfo structInfo;

	// Чтобы компилировался Cell.fromJsonArray, хотя он не используется
	public Cell()
	{
		MyAssert.a(false);
	}

	// Для редактора карт
	public Cell(Game game, CellType type)
	{
		setGame(game);
		this.type = type;
	}
	
	// тоже
	public Cell(Cell cell)
	{
		this(cell.game, cell.type);
		player = cell.player;
	}
	
	// в принципе можно прямо тут обновлять fieldCells
	public Cell(Game game, CellType type, int i, int j)
	{
		this(game, type);
		this.i = i;
		this.j = j;
	}
	
	public void destroy()
	{
		game.fieldCells[i][j] = new Cell(game, type.destroyingType, i, j);
	}
	
	public void repair()
	{
		game.fieldCells[i][j] = new Cell(game, type.repairType, i, j);
	}
	
	public boolean needSave()
	{
		return isCapture();
	}
	
	public void save(DataOutputStream output, Game game) throws Exception
	{
		output.writeBoolean(isCapture());
		if (isCapture())
			output.write(player.ordinal);
	}
	
	public void load(DataInputStream input, LoaderInfo info) throws Exception
	{
		boolean isCapture = input.readBoolean();
		if (isCapture)
			player = game.players[input.read()];
	}
	
	public int getSteps()
	{
		return type.steps;
	}
	
	public Team getTeam()
	{
		return player == null ? null : player.team;
	}
	
	@Override
	public boolean equals(Object o)
	{
		Cell cell = (Cell) o;
		if (this == cell)
			return true;
		if (type != cell.type)
			return false;
		if (i != cell.i)
			return false;
		if (j != cell.j)
			return false;
		if (isCapture() != cell.isCapture())
			return false;
		if (player == null)
		{
			if (cell.player != null)
				return false;
		}
		else if (player.ordinal != cell.player.ordinal)
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (%d %d)", type.name, i, j);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = new JsonObject();
		object.addProperty("i", i);
		object.addProperty("j", j);
		if (player != null)
			object.addProperty("player", player.getNumber());
		if (structInfo != null)
			object.add("structInfo", structInfo.toJson());
		return object;
	}

	public Cell fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		game = info.game;
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		if (object.has("player"))
			player = Player.newInstance(object.get("player").getAsInt(), info);
		if (object.has("structInfo"))
			structInfo = info.fromJson((JsonObject) object.get("structInfo"), StructInfo.class);
		return this;
	}

	static public Cell[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Cell[] array = new Cell[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = new Cell().fromJson((com.google.gson.JsonObject) jsonArray.get(i), info);
		return array;
	}

}
