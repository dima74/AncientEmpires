package ru.ancientempires.campaign.conditions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class ConditionUnitIntoBounds extends Condition
{
	
	private Player           player;
	private AbstractBounds[] bounds;
	
	public ConditionUnitIntoBounds()
	{
	}
	
	public ConditionUnitIntoBounds(int player, AbstractBounds... bounds)
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
		for (Unit unit : player.units)
			for (AbstractBounds bounds : this.bounds)
				if (bounds.in(unit.i, unit.j))
					return true;
		return false;
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("bounds");
		new Gson().toJson(bounds, Bounds[].class, writer);
		writer.name("player").value(player.ordinal);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("player", player.getNumber());
		object.add("bounds", SerializableJsonHelper.toJsonArray(bounds));
		return object;
	}

	public ConditionUnitIntoBounds fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		player = Player.newInstance(object.get("player").getAsInt(), info);
		bounds = AbstractBounds.fromJsonArray(object.get("bounds").getAsJsonArray(), info);
		return this;
	}

}
