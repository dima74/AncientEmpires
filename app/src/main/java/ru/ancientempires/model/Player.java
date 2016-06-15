package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import ru.ancientempires.MyColor;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Numbered;
import ru.ancientempires.serializable.SerializableJson;

public class Player implements SerializableJson, Numbered
{

	public static Player newInstance(int i, LoaderInfo info)
	{
		return info.game.players[i];
	}

	@Exclude
	public int     ordinal;
	public MyColor color;

	public PlayerType      type;
	public Team            team;
	@Exclude
	public ArrayList<Unit> units;
	public int             gold;

	public int cursorI;
	public int cursorJ;

	public Player()
	{}

	@Override
	public int getNumber()
	{
		return ordinal;
	}

	public JsonObject json()
	{
		JsonObject object = new JsonObject();
		object.addProperty("color", color.name());
		object.addProperty("ordinal", ordinal);
		object.addProperty("type", type.name());
		object.addProperty("gold", gold);
		object.addProperty("cursorI", cursorI);
		object.addProperty("cursorJ", cursorJ);
		return object;
	}
	
	public int numberUnits()
	{
		return units.size();
	}
	
	@Override
	public boolean equals(Object o)
	{
		Player player = (Player) o;
		if (color != player.color)
			return false;
		if (ordinal != player.ordinal)
			return false;
		if (type != player.type)
			return false;
		if (gold != player.gold)
			return false;
		// ActionPlayerChangeCursorPosition
		/*
		if (cursorI != player.cursorI)
			return false;
		if (cursorJ != player.cursorJ)
			return false;
		*/
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Player [" + units + "]";
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = new JsonObject();
		object.addProperty("color", color.name());
		object.addProperty("type", type.name());
		object.addProperty("team", team.getNumber());
		object.addProperty("gold", gold);
		object.addProperty("cursorI", cursorI);
		object.addProperty("cursorJ", cursorJ);
		return object;
	}

	public Player fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		color = MyColor.valueOf(object.get("color").getAsString());
		type = PlayerType.valueOf(object.get("type").getAsString());
		team = Team.newInstance(object.get("team").getAsInt(), info);
		gold = object.get("gold").getAsInt();
		cursorI = object.get("cursorI").getAsInt();
		cursorJ = object.get("cursorJ").getAsInt();
		return this;
	}

	static public Player[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Player[] array = new Player[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), Player.class);
		return array;
	}

}
