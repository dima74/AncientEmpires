package ru.ancientempires.campaign.conditions;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.helpers.JsonHelper;

public class ConditionTurn extends Script
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
		return GameHandler.game.currentTurn == turn;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("turn").value(turn);
	}
	
}
