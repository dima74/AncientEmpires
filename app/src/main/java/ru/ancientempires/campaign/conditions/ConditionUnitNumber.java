package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

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

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
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
