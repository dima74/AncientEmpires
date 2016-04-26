package ru.ancientempires.campaign.scripts;

import ru.ancientempires.action.campaign.AcionCampaignUnitCreate;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public class ScriptUnitCreate extends ScriptOnePoint
{
	
	private AbstractPoint point;
	private UnitType      unitType;
	private Player        player;
	
	public ScriptUnitCreate()
	{
	}
	
	public ScriptUnitCreate(int player, String unitType, Object... point)
	{
		this.player = getGame().players[player];
		this.unitType = "KING".equals(unitType) ? new UnitHelper(getGame()).getKingType(this.player) : getGame().rules.getUnitType(unitType);
		this.point = AbstractPoint.createPoint(point);
	}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitCreate(i(), j(), unitType, player, this);
	}
	
	@Override
	public void performAction()
	{
		new AcionCampaignUnitCreate().setType(unitType).setPlayer(player.ordinal).setIJ(i(), j()).perform(game);
	}
	
}
