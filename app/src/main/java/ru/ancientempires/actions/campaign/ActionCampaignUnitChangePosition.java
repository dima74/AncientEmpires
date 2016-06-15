package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.ActionFromTo;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignUnitChangePosition extends ActionFromTo
{
	
	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		MyAssert.a(unit != null);
		game.removeUnit(i, j);
		game.setUnit(targetI, targetJ, unit);
	}

	@Override
	public boolean check()
	{
		return game.getUnit(i, j) != null && game.canSetUnit(targetI, targetJ);
	}
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionCampaignUnitChangePosition fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
