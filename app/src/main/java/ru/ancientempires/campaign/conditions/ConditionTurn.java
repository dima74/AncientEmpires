package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.serializable.LoaderInfo;

public class ConditionTurn extends Condition
{
	
	private int turn;
	
	public ConditionTurn()
	{}
	
	public ConditionTurn(int turn)
	{
		this.turn = turn;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		turn = JsonHelper.readInt(reader, "turn");
	}
	
	@Override
	public boolean check()
	{
		return game.currentTurn == turn;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("turn").value(turn);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("turn", turn);
		return object;
	}

	public ConditionTurn fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		turn = object.get("turn").getAsInt();
		return this;
	}

}
