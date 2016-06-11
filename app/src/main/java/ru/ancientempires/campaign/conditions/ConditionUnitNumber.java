package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.serializable.LoaderInfo;

public class ConditionUnitNumber extends Condition
{
	
	private Player player;
	private int    comparator;    // -1 less, 0 equals, +1 more
	private int    number;
	
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
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("player", player.getNumber());
		object.addProperty("comparator", comparator);
		object.addProperty("number", number);
		return object;
	}

	public ConditionUnitNumber fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		player = Player.newInstance(object.get("player").getAsInt(), info);
		comparator = object.get("comparator").getAsInt();
		number = object.get("number").getAsInt();
		return this;
	}

}
