package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.action.campaign.AcionCampaignUnitCreate;
import ru.ancientempires.campaign.Coordinate;
import ru.ancientempires.campaign.CoordinateInteger;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public class ScriptUnitCreate extends Script
{
	
	private Coordinate	i;
	private Coordinate	j;
	private UnitType	unitType;
	private Player		player;
	
	public ScriptUnitCreate()
	{}
	
	public ScriptUnitCreate(Coordinate i, Coordinate j, String unitType, int player)
	{
		this.i = i;
		this.j = j;
		this.player = getGame().players[player];
		this.unitType = "KING".equals(unitType) ? new UnitHelper(getGame()).getKingType(this.player) : getGame().rules.getUnitType(unitType);
	}
	
	public ScriptUnitCreate(int i, int j, String unitType, int player)
	{
		this.i = new CoordinateInteger(i);
		this.j = new CoordinateInteger(j);
		this.unitType = getGame().rules.getUnitType(unitType);
		this.player = getGame().players[player];
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		i = Coordinate.getNew(reader, "i");
		j = Coordinate.getNew(reader, "j");
		unitType = game.rules.getUnitType(JsonHelper.readString(reader, "unitType"));
		player = game.players[JsonHelper.readInt(reader, "player")];
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitCreate(i.get(), j.get(), unitType, player, this);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		i.save(writer, "i");
		j.save(writer, "j");
		writer.name("unitType").value(unitType.name);
		writer.name("player").value(player.ordinal);
	}
	
	@Override
	public void performAction()
	{
		new AcionCampaignUnitCreate()
				.setType(unitType)
				.setPlayer(player.ordinal)
				.setIJ(i.get(), j.get())
				.perform(game);
	}
	
}
