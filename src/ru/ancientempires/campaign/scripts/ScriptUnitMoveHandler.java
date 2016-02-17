package ru.ancientempires.campaign.scripts;

import ru.ancientempires.images.bitmaps.UnitBitmap;

public abstract class ScriptUnitMoveHandler extends Script
{
	
	public boolean complete;
	
	public abstract void unitMove(UnitBitmap unit);
	
	public void complete()
	{
		if (!complete)
			campaign.iDrawCampaign.updateCampaign();
		complete = true;
	}
	
	@Override
	public boolean check()
	{
		return super.check() && complete;
	}
	
}
