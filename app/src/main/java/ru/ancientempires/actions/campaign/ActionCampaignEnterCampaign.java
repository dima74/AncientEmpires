package ru.ancientempires.actions.campaign;

import ru.ancientempires.actions.Action;

public class ActionCampaignEnterCampaign extends Action
{

	@Override
	public void performQuick()
	{
		game.path.enterCampaign();
	}

}
