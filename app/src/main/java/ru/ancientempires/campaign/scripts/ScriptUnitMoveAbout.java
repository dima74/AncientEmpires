package ru.ancientempires.campaign.scripts;

import ru.ancientempires.action.campaign.ActionCampaignUnitMove;
import ru.ancientempires.campaign.points.PointInteger;
import ru.ancientempires.framework.MyAssert;

public class ScriptUnitMoveAbout extends ScriptUnitMove
{
	
	public ScriptUnitMoveAbout()
	{
	}
	
	public ScriptUnitMoveAbout(Object... points)
	{
		super(points);
		MyAssert.a(this.points.length == 2);
	}
	
	@Override
	public void start()
	{
		find();
		campaign.iDrawCampaign.unitMove(this, true);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignUnitMove()
				.setIJ(i(), j())
				.setTargetIJ(targetI(), targetJ())
				.perform(game);
	}
	
	private void find()
	{
		for (int a = 0; ; a++)
			for (int i = -a; i <= a; i++)
				for (int j = -a; j <= a; j++)
					if (Math.abs(i) + Math.abs(j) == a)
						if (tryIJ(i, j))
							return;
	}
	
	private boolean tryIJ(int relativeI, int relativeJ)
	{
		int absoluteI = targetI() + relativeI;
		int absoluteJ = targetJ() + relativeJ;
		if (game.checkCoordinates(absoluteI, absoluteJ) && game.fieldUnits[absoluteI][absoluteJ] == null)
		{
			points[points.length - 1] = new PointInteger(absoluteI, absoluteJ);
			return true;
		}
		return false;
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
}
