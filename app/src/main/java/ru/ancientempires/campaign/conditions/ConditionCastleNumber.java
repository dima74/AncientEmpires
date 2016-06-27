package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Player;
import ru.ancientempires.serializable.LoaderInfo;

public class ConditionCastleNumber extends Condition
{
	
	private Player player;
	private int    comparator;    // -1 less, 0 equals, +1 more
	private int    number;
	
	public ConditionCastleNumber()
	{}
	
	public ConditionCastleNumber(int player, int comparator, int value)
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
		int number = getCastleNumber(player);
		return ConditionCastleNumber.sign(this.number - number) == comparator;
	}
	
	public int getCastleNumber(Player player)
	{
		CellType castle = game.rules.getCellType("CASTLE");
		int number = 0;
		for (Cell[] line : game.fieldCells)
			for (Cell cell : line)
				if (cell.type == castle && cell.player == player)
					number++;
		return number;
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

	public ConditionCastleNumber fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		player = Player.newInstance(object.get("player").getAsInt(), info);
		comparator = object.get("comparator").getAsInt();
		number = object.get("number").getAsInt();
		return this;
	}

}
