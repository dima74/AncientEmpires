package ru.ancientempires.campaign.conditions;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Unit;

public class ConditionNamedUnitIntoBounds extends Condition
{
	
	private String           name;
	private AbstractBounds[] bounds;
	
	public ConditionNamedUnitIntoBounds()
	{
	}
	
	public ConditionNamedUnitIntoBounds(String name, AbstractBounds... bounds)
	{
		this.name = name;
		this.bounds = bounds;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		MyAssert.a("bounds", reader.nextName());
		bounds = new Gson().fromJson(reader, Bounds[].class);
		name = JsonHelper.readString(reader, "name");
	}
	
	@Override
	public boolean check()
	{
		Unit unit = game.namedUnits.get(name);
		for (AbstractBounds bounds : this.bounds)
			if (bounds.in(unit.i, unit.j))
				return true;
		return false;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("bounds");
		new Gson().toJson(bounds, Bounds[].class, writer);
		writer.name("name").value(name);
	}
	
}
