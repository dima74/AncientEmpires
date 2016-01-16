package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;

public class GameDrawCellAttack extends GameDrawOnFramesGroup
{
	
	private int	targetI;
	private int	targetJ;
	private int	frameUpdateTargetCell	= -1;
	
	public void start(ActionResult result, int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		int y = targetI * A;
		int x = targetJ * A;
		
		draws.clear();
		add(new GameDrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(1));
		frameUpdateTargetCell = frameEnd;
		add(new GameDrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1)
				.increaseFrameStart(frameCount));
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (iFrame() == frameUpdateTargetCell)
		{
			main.gameDrawCells.updateOneCell(targetI, targetJ);
			main.gameDrawCellDual.updateOneCell(targetI, targetJ);
		}
	}
	
}
