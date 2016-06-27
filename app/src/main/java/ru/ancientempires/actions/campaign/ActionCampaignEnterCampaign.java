package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.Action;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignEnterCampaign extends Action
{

	@Override
	public void performQuick()
	{
		if (game.isSaver)
			game.path.enterCampaign();
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionCampaignEnterCampaign fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
