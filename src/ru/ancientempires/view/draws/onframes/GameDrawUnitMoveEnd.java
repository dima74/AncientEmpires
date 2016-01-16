package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.model.Unit;

public class GameDrawUnitMoveEnd extends GameDrawOnFramesGroup
{
	
	public void start(ActionResultUnitMove result, int frameToStart)
	{
		draws.clear();
		if (result.sign == +1)
			for (Unit unit : result.units)
				add(new GameDrawBitmaps()
						.setYX(unit.i * A, unit.j * A)
						.setBitmaps(SparksImages().bitmapsDefault)
						.animateRepeat(1)
						.increaseFrameStart(frameToStart));
	}
	
}
