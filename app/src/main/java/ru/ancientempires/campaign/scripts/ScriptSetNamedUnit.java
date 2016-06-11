package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.action.campaign.ActionCampaignSetNamedUnit;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetNamedUnit extends Script
{
	
	private int    i;
	private int    j;
	private String name;
	
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
		i = JsonHelper.readInt(reader, "i");
		j = JsonHelper.readInt(reader, "j");
		name = JsonHelper.readString(reader, "name");
	}
	
	@Override
	public void start()
	{
		new ActionCampaignSetNamedUnit(name)
				.setIJ(i, j)
				.perform(game);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(i);
		writer.name("j").value(j);
		writer.name("name").value(name);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("i", i);
		object.addProperty("j", j);
		object.addProperty("name", name);
		return object;
	}

	public ScriptSetNamedUnit fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		name = object.get("name").getAsString();
		return this;
	}

}
