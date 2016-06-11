package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptCameraMoveOnKing extends AbstractScriptOnePoint
{
	
	public Player player;
	
	public ScriptCameraMoveOnKing()
	{}
	
	public ScriptCameraMoveOnKing(int player)
	{
		this.player = getGame().players[player];
	}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.cameraMove(this);
	}

	@Override
	public int i()
	{
		return new UnitHelper(game).getKing(player).i;
	}

	@Override
	public int j()
	{
		return new UnitHelper(game).getKing(player).j;
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("player", player.getNumber());
		return object;
	}

	public ScriptCameraMoveOnKing fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		player = Player.newInstance(object.get("player").getAsInt(), info);
		return this;
	}

}
