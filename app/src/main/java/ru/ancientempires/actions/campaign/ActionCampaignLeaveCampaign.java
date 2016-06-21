package ru.ancientempires.actions.campaign;

import ru.ancientempires.actions.Action;

public class ActionCampaignLeaveCampaign extends Action
{

	@Override
	public void performQuick()
	{
		game.path.leaveCampaign();
	}

}
