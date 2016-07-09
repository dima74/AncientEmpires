package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Objects;

import ru.ancientempires.MyColor;
import ru.ancientempires.serializable.EraseNulls;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.MyNullableTo;
import ru.ancientempires.serializable.Numbered;
import ru.ancientempires.serializable.SerializableJson;

@EraseNulls
public class Player implements SerializableJson, Numbered
{

	public static Player newInstance(int i, LoaderInfo info)
	{
		return info.game.players[i];
	}

	@Exclude
	public int     ordinal;
	@MyNullableTo
	public MyColor color;

	@MyNullableTo
	public PlayerType      type;
	@MyNullableTo
	public Team            team;
	@Exclude
	public ArrayList<Unit> units;
	public Integer         gold;
	public Integer         unitsLimit;

	@Override
	public int getNumber()
	{
		return ordinal;
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
		if (!Objects.equals(gold, player.gold))
			return false;
		if (!Objects.equals(unitsLimit, player.unitsLimit))
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Player [" + units + "]";
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = new JsonObject();
		if (color != null)
			object.addProperty("color", color.name());
		if (type != null)
			object.addProperty("type", type.name());
		if (team != null)
			object.addProperty("team", team.getNumber());
		object.addProperty("gold", gold);
		object.addProperty("unitsLimit", unitsLimit);
		ru.ancientempires.serializable.SerializableJsonHelper.eraseNulls(object);
		return object;
	}

	public Player fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		color = MyColor.valueOf(object.get("color").getAsString());
		type = PlayerType.valueOf(object.get("type").getAsString());
		team = Team.newInstance(object.get("team").getAsInt(), info);
		gold = object.get("gold").getAsInt();
		unitsLimit = object.get("unitsLimit").getAsInt();
		return this;
	}

	static public Player[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Player[] array = new Player[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = new Player().fromJson((com.google.gson.JsonObject) jsonArray.get(i), info);
		return array;
	}

}
