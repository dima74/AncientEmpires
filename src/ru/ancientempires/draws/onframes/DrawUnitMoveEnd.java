package ru.ancientempires.draws.onframes;

import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.model.Unit;

public class DrawUnitMoveEnd extends DrawOnFramesGroup
{
	
	public DrawUnitMoveEnd(ActionResultUnitMove result, int frameToStart)
	{
		draws.clear();
		if (result.sign > 0)
			for (Unit unit : result.units)
				add(new DrawBitmaps()
						.setYX(unit.i * A, unit.j * A)
						.setBitmaps(SparksImages().bitmapsDefault)
						.animateRepeat(1)
						.increaseFrameStart(frameToStart));
	}
	
}
