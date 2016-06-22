package ru.ancientempires.actions.campaign;

import ru.ancientempires.actions.Action;

public class ActionCampaignLeaveCampaign extends Action
{

	@Override
	public void performQuick()
	{
		if (game.isSaver)
			game.path.leaveCampaign();
	}

}
