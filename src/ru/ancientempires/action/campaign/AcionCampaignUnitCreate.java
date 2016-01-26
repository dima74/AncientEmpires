package ru.ancientempires.action.campaign;

import ru.ancientempires.action.ActionFrom;
import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class AcionCampaignUnitCreate extends ActionFrom
{
	
	public UnitType	type;
	public Player	player;
	
	public AcionCampaignUnitCreate setType(UnitType type)
	{
		this.type = type;
		return this;
	}
	
	public AcionCampaignUnitCreate setPlayer(Player player)
	{
		this.player = player;
		return this;
	}
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}
	
	@Override
	public ActionResult perform(Game game)
	{
		performBase(game);
		return null;
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = new Unit(type, player, game);
		player.units.add(unit);
		game.setUnit(i, j, unit);
	}
	
}
