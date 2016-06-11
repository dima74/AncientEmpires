package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.model.Unit;

public class DrawUnitMoveEnd extends DrawOnFramesGroup
{
	
	private int    frameUpdateBonus;
	private Unit[] units;

	public DrawUnitMoveEnd(ActionResultUnitMove result, int frameToStart)
	{
		draws.clear();
		if (result.sign > 0)
		{
			units = result.units;
			for (Unit unit : result.units)
			{
				DrawOnFrames draw = new DrawBitmaps()
						.setYX(unit.i * A, unit.j * A)
						.setBitmaps(SparksImages().bitmapsDefault)
						.animateRepeat(1)
						.increaseFrameStart(frameToStart);
				add(draw);
				main.units.field[unit.i][unit.j].canUpdatePositiveBonus = false;
				frameUpdateBonus = draw.frameEnd;
			}
		}
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (iFrame() == frameUpdateBonus)
			for (Unit unit : units)
				main.units.field[unit.i][unit.j].canUpdatePositiveBonus = true;
	}
	
}
