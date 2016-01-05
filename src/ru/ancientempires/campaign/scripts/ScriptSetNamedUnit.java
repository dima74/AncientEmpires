package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.helpers.JsonHelper;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ScriptSetNamedUnit extends Script
{
	
	private int		i;
	private int		j;
	private String	name;
	
	public ScriptSetNamedUnit()
	{}
	
	public ScriptSetNamedUnit(int i, int j, String name)
	{
		this.i = i;
		this.j = j;
		this.name = name;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		this.i = JsonHelper.readInt(reader, "i");
		this.j = JsonHelper.readInt(reader, "j");
		this.name = JsonHelper.readString(reader, "name");
	}
	
	@Override
	public void start()
	{
		super.start();
		NamedUnits.set(this.name, GameHandler.getUnit(this.i, this.j));
		campaign.finish(this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(this.i);
		writer.name("j").value(this.j);
		writer.name("name").value(this.name);
	}
	
}
