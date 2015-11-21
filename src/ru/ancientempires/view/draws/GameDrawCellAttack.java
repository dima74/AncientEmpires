package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.images.SparksImages;
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
		int y = targetI * GameDraw.A;
		int x = targetJ * GameDraw.A;
		
		draws.clear();
		add(new GameDrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages.bitmapsAttack)
				.animateRepeat(1));
		frameUpdateTargetCell = frameEnd;
		add(new GameDrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages.bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1)
				.increaseFrameStart(frameCount));
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (GameDraw.iFrame == frameUpdateTargetCell)
		{
			GameDraw.main.gameDrawCells.updateOneCell(targetI, targetJ);
			GameDraw.main.gameDrawCellDual.updateOneCell(targetI, targetJ);
		}
	}
	
}
