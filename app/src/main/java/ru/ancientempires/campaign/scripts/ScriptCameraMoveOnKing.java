package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.points.PointInteger;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class ScriptCameraMoveOnKing extends ScriptCameraMove
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
		Unit king = new UnitHelper(game).getKing(player);
		point = new PointInteger(king.i, king.j);
		campaign.iDrawCampaign.cameraMove(this);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
}
