package ru.ancientempires.campaign.conditions;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Player;

public class ConditionUnitNumber extends Condition
{
	
	private Player	player;
	private int		comparator;	// -1 less, 0 equals, +1 more
	private int		number;
	
	public ConditionUnitNumber()
	{}
	
	public ConditionUnitNumber(int player, int comparator, int value)
	{
		this.player = getGame().players[player];
		this.comparator = comparator;
		number = value;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		player = game.players[JsonHelper.readInt(reader, "player")];
		comparator = JsonHelper.readInt(reader, "comparator");
		number = JsonHelper.readInt(reader, "number");
	}
	
	private static int sign(int x)
	{
		return x == 0 ? 0 : x < 0 ? -1 : 1;
	}
	
	@Override
	public boolean check()
	{
		int number = player.units.size();
		return ConditionUnitNumber.sign(this.number - number) == comparator;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("player").value(player.ordinal);
		writer.name("comparator").value(comparator);
		writer.name("number").value(number);
	}
	
}