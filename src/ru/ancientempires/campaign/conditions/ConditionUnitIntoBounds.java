package ru.ancientempires.campaign.conditions;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class ConditionUnitIntoBounds extends Condition
{
	
	private Player		player;
	private Bounds[]	bounds;
	
	public ConditionUnitIntoBounds()
	{}
	
	public ConditionUnitIntoBounds(int player, Bounds... bounds)
	{
		this.player = getGame().players[player];
		this.bounds = bounds;
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		MyAssert.a("bounds", reader.nextName());
		bounds = new Gson().fromJson(reader, Bounds[].class);
		player = game.players[JsonHelper.readInt(reader, "player")];
	}
	
	@Override
	public boolean check()
	{
		for (Bounds bounds : this.bounds)
			for (int i = bounds.iMin; i <= bounds.iMax; i++)
				for (int j = bounds.jMin; j <= bounds.jMax; j++)
				{
					Unit unit = game.getUnit(i, j);
					if (unit != null && unit.player == player)
						return true;
				}
		return false;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("bounds");
		new Gson().toJson(bounds, Bounds[].class, writer);
		writer.name("player").value(player.ordinal);
	}
	
}
