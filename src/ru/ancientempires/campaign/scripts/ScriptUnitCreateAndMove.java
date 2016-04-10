package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.Point;
import ru.ancientempires.action.campaign.AcionCampaignUnitCreate;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public class ScriptUnitCreateAndMove extends Script
{
	
	public UnitType	unitType;
	public Player	player;
	public Point[]	keyPoints;
	public int[]	handlers;
	public boolean	makeSmoke	= true;
								
	public ScriptUnitMoveHandler[] getHandlers()
	{
		ScriptUnitMoveHandler[] handlersScript = new ScriptUnitMoveHandler[handlers.length];
		for (int i = 0; i < handlersScript.length; i++)
			handlersScript[i] = (ScriptUnitMoveHandler) campaign.scripts[handlers[i]];
		return handlersScript;
	}
	
	// CampaignEditor
	public ScriptUnitMoveHandler[] handlersScript;
	
	public ScriptUnitCreateAndMove()
	{}
	
	public ScriptUnitCreateAndMove(String unitType, int player)
	{
		this.player = getGame().players[player];
		this.unitType = "KING".equals(unitType) ? new UnitHelper(getGame()).getKingType(this.player) : getGame().rules.getUnitType(unitType);
	}
	
	public ScriptUnitCreateAndMove setPoints(int... points)
	{
		MyAssert.a(points.length % 2 == 0);
		keyPoints = new Point[points.length / 2];
		for (int i = 0; i < keyPoints.length; i++)
			keyPoints[i] = new Point(points[i * 2], points[i * 2 + 1]);
		return this;
	}
	
	public ScriptUnitCreateAndMove setHandlers(ScriptUnitMoveHandler... handlers)
	{
		handlersScript = handlers;
		return this;
	}
	
	public ScriptUnitCreateAndMove disableMakeSmoke()
	{
		makeSmoke = false;
		return this;
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitCreateAndMove(this);
	}
	
	@Override
	public void performAction()
	{
		Point last = last();
		new AcionCampaignUnitCreate()
				.setType(unitType)
				.setPlayer(player.ordinal)
				.setIJ(last.i, last.j)
				.perform(game);
	}
	
	public Point last()
	{
		return keyPoints[keyPoints.length - 1];
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("unitType").value(unitType.name);
		writer.name("player").value(player.ordinal);
		writer.name("keyPoints");
		new Gson().toJson(keyPoints, Point[].class, writer);
		writer.name("makeSmoke").value(makeSmoke);
		
		if (handlersScript == null)
			handlersScript = new ScriptUnitMoveHandler[0];
		writer.name("handlers");
		int[] indexes = new int[handlersScript.length];
		for (int i = 0; i < indexes.length; i++)
			indexes[i] = handlersScript[i].index;
		new Gson().toJson(indexes, int[].class, writer);
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		unitType = game.rules.getUnitType(JsonHelper.readString(reader, "unitType"));
		player = game.players[JsonHelper.readInt(reader, "player")];
		MyAssert.a("keyPoints", reader.nextName());
		keyPoints = new Gson().fromJson(reader, Point[].class);
		makeSmoke = JsonHelper.readBoolean(reader, "makeSmoke");
		
		MyAssert.a("handlers", reader.nextName());
		handlers = new Gson().fromJson(reader, int[].class);
	}
	
}
