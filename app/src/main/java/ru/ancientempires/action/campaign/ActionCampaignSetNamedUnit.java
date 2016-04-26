package ru.ancientempires.action.campaign;

import ru.ancientempires.action.ActionFrom;

public class ActionCampaignSetNamedUnit extends ActionFrom
{
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}
	
	public String name;
	
	public ActionCampaignSetNamedUnit(String name)
	{
		this.name = name;
	}
	
	@Override
	public void performQuick()
	{
		game.namedUnits.set(name, game.getUnit(i, j));
	}
	
}
