package ru.ancientempires.campaign.scripts;

public class ScriptUnitAttack extends ScriptFrom
{
	
	public ScriptUnitAttack()
	{}
	
	public ScriptUnitAttack(int i, int j)
	{
		super(i, j);
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitAttack(i, j, this);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
}
