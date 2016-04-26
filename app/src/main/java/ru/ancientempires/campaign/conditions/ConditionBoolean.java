package ru.ancientempires.campaign.conditions;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.reflection.NumberedArray;

public abstract class ConditionBoolean extends Condition
{

	@NumberedArray
	public Script[] scripts;
	
	public ConditionBoolean()
	{
	}
	
	public ConditionBoolean(Script... scripts)
	{
		this.scripts = scripts;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("scripts");
		int[] indexes = new int[scripts.length];
		for (int i = 0; i < indexes.length; i++)
			indexes[i] = scripts[i].index;
		new Gson().toJson(indexes, int[].class, writer);
	}
	
	@Override
	public void load(JsonReader reader, ArrayList<Script> scripts) throws IOException
	{
		MyAssert.a("scripts", reader.nextName());
		int[] indexes = new Gson().fromJson(reader, int[].class);
		this.scripts = new Script[indexes.length];
		for (int i = 0; i < indexes.length; i++)
			this.scripts[i] = scripts.get(indexes[i]);
	}

	public void resolveAliases(Script[] scripts)
	{
		super.resolveAliases(scripts);
		resolveAliases(this.scripts, scripts);
	}
	
}
