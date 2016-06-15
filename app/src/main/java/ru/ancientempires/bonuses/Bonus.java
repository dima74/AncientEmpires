package ru.ancientempires.bonuses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.SerializableJsonHelper;

@IndexSubclasses
public abstract class Bonus implements SerializableJson
{
	
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit)
	{
		return 0;
	}
	
	public final int getBonusAttack(Game game, Unit unit, Unit targetUnit)
	{
		return getBonusAttack(game, unit, unit.getCell(), targetUnit);
	}
	
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit)
	{
		return 0;
	}
	
	public final int getBonusDefence(Game game, Unit unit, Unit fromUnit)
	{
		return getBonusDefence(game, unit, unit.getCell(), fromUnit);
	}
	
	public int getBonusMove(Game game, Unit unit, Cell cell, Cell targetCell)
	{
		return 0;
	}
	
	public int getBonusMoveStart(Game game, Unit unit)
	{
		return 0;
	}
	
	public int getBonusCost(Game game, Unit unit)
	{
		return 0;
	}
	
	public int getSign()
	{
		return 0;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = SerializableJsonHelper.toJson(this);
		return object;
	}

	public Bonus fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		return this;
	}

	static public Bonus[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Bonus[] array = new Bonus[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), Bonus.class);
		return array;
	}

}
