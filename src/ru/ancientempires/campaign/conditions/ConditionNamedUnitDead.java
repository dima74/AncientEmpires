package ru.ancientempires.campaign.conditions;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Unit;

public class ConditionNamedUnitDead extends Script
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
		Unit unit = NamedUnits.get(name);
		return GameHandler.getUnit(unit.i, unit.j) != unit;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("name").value(name);
	}
	
}
