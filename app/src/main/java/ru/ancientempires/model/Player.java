package ru.ancientempires.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import ru.ancientempires.MyColor;
import ru.ancientempires.PlayerType;
import ru.ancientempires.reflection.LoaderInfo;
import ru.ancientempires.reflection.Numbered;

public class Player implements Numbered
{

	public static Player newInstance(int i, LoaderInfo info)
	{
		return info.game.players[i];
	}

	public MyColor color;
	public int     ordinal;

	public PlayerType      type  = PlayerType.PLAYER;
	public Team            team  = new Team(new Player[]
			{
					this
			});
	public ArrayList<Unit> units = new ArrayList<Unit>();
	public int gold;

	public int cursorI;
	public int cursorJ;

	public Player()
	{
	}
	
	public Player(int ordinal)
	{
		this.ordinal = ordinal;
		color = MyColor.playersColors()[ordinal];
	}
	
	public Player(JsonObject object)
	{
		color = MyColor.valueOf(object.get("color").getAsString());
		ordinal = object.get("ordinal").getAsInt();
		type = PlayerType.valueOf(object.get("type").getAsString());
		gold = object.get("gold").getAsInt();
		cursorI = object.get("cursorI").getAsInt();
		cursorJ = object.get("cursorJ").getAsInt();
	}

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
	
}
