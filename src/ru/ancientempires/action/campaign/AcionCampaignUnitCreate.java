package ru.ancientempires.action.campaign;

import ru.ancientempires.action.ActionFrom;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class AcionCampaignUnitCreate extends ActionFrom
{
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}
	
	public UnitType	type;
	public int		player;
					
	public AcionCampaignUnitCreate setType(UnitType type)
	{
		this.type = type;
		return this;
	}
	
	public AcionCampaignUnitCreate setPlayer(int player)
	{
		this.player = player;
		return this;
	}
	
	@Override
	public void performQuick()
	{
		Player player = game.players[this.player];
		Unit unit = new Unit(game, type, player);
		player.units.add(unit);
		game.setUnit(i, j, unit);
	}
	
}
