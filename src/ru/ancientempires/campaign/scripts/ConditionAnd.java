package ru.ancientempires.campaign.scripts;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.campaign.conditions.Condition;
import ru.ancientempires.framework.MyAssert;

public class ConditionAnd extends Condition
{
	
	private Script[] scripts;
	
	public ConditionAnd()
	{}
	
	public ConditionAnd(Script... scripts)
	{
		this.scripts = scripts;
	}
	
	@Override
	public boolean check()
	{
		for (Script script : previous)
			if (!script.check())
				return false;
		return true;
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
	
}
