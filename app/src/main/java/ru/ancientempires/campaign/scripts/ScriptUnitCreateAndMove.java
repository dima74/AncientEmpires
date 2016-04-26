package ru.ancientempires.campaign.scripts;

import ru.ancientempires.action.campaign.AcionCampaignUnitCreate;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.UnitType;

public class ScriptUnitCreateAndMove extends ScriptUnitMove
{
	
	public UnitType unitType;
	public Player player;

	public ScriptUnitCreateAndMove()
	{
	}
	
	public ScriptUnitCreateAndMove(int player, String unitType, Object... points)
	{
		super(points);
		this.player = getGame().players[player];
		this.unitType = "KING".equals(unitType) ? new UnitHelper(getGame()).getKingType(this.player) : getGame().rules.getUnitType(unitType);
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitMove(this, false);
	}
	
	@Override
	public void performAction()
	{
		new AcionCampaignUnitCreate()
				.setType(unitType)
				.setPlayer(player.ordinal)
				.setIJ(targetI(), targetJ())
				.perform(game);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
}
