package ru.ancientempires.campaign.conditions;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Unit;

public class ConditionNamedUnitDead extends Condition
{
	
	private String name;
	
	public ConditionNamedUnitDead()
	{}
	
	public ConditionNamedUnitDead(String name)
	{
		this.name = name;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		name = JsonHelper.readString(reader, "name");
	}
	
	@Override
	public boolean check()
	{
		Unit unit = game.namedUnits.get(name);
		return game.getUnit(unit.i, unit.j) != unit;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("name").value(name);
	}
	
}
