package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.images.bitmaps.UnitBitmap;

public class ScriptUnitMoveHandlerPoint extends ScriptUnitMoveHandler
{
	
	public AbstractPoint point;
	
	public ScriptUnitMoveHandlerPoint()
	{
	}
	
	public ScriptUnitMoveHandlerPoint(Object... point)
	{
		this.point = AbstractPoint.createPoint(point);
	}
	
	@Override
	public void unitMove(UnitBitmap unit)
	{
		if (unit.exactlyOn(point))
			complete();
	}
	
}
