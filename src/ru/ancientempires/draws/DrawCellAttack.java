package ru.ancientempires.draws;

import android.graphics.Canvas;
import ru.ancientempires.GameThread;
import ru.ancientempires.action.result.ActionResultUnitAttack;
import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawCellAttack extends DrawOnFramesGroup
{
	
	private int	targetI;
	private int	targetJ;
	private int	frameUpdateTargetCell	= -1;
										
	public DrawCellAttack(ActionResultUnitAttack result, int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		int y = targetI * A;
		int x = targetJ * A;
		
		draws.clear();
		add(new DrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(1));
		frameUpdateTargetCell = frameEnd;
		add(new DrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1)
				.increaseFrameStart(frameCount));
				
		main.cells.keep[targetI][targetJ] = true;
		main.cellsDual.keep[targetI][targetJ] = true;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (iFrame() == frameUpdateTargetCell)
		{
			main.cells.keep[targetI][targetJ] = false;
			main.cellsDual.keep[targetI][targetJ] = false;
		}
	}
	
	@Override
	public void onEnd()
	{
		GameThread.thread.needUpdateCampaign = true;
	}
	
}
