package ru.ancientempires.campaign.scripts;

import ru.ancientempires.action.campaign.ActionCampaignCellAttack;

public class ScriptCellAttackPartTwo extends ScriptFrom
{
	
	public ScriptCellAttackPartTwo()
	{
	}
	
	public ScriptCellAttackPartTwo(int i, int j)
	{
		super(i, j);
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.cellAttackPartTwo(i, j, this);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignCellAttack()
				.setIJ(i, j)
				.perform(game);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}

}
