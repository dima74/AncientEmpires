package ru.ancientempires.campaign.conditions;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Unit;

public class ConditionNamedUnitIntoBounds extends Script
{
	
	private String		name;
	private Bounds[]	bounds;
	
	public ConditionNamedUnitIntoBounds()
	{}
	
	public ConditionNamedUnitIntoBounds(String name, Bounds... bounds)
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
		Unit unit = NamedUnits.get(name);
		for (Bounds bounds : this.bounds)
			if (bounds.iMin <= unit.i && unit.i <= bounds.iMax && bounds.jMin <= unit.j && unit.j <= bounds.jMax)
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
