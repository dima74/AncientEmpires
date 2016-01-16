package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class ScriptCameraMoveOnKing extends Script
{
	
	private Player player;
	
	public ScriptCameraMoveOnKing()
	{}
	
	public ScriptCameraMoveOnKing(int player)
	{
		this.player = getGame().players[player];
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		player = campaign.game.players[JsonHelper.readInt(reader, "player")];
	}
	
	@Override
	public void start()
	{
		super.start();
		Unit king = new UnitHelper().getKing(player);
		campaign.iDrawCampaign.cameraMove(king.i, king.j, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("player").value(player.ordinal);
	}
	
}
