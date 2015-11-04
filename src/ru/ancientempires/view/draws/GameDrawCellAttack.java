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
	
	public GameDrawCellAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(ActionResult result, int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		int y = targetI * GameDraw.A;
		int x = targetJ * GameDraw.A;
		
		this.draws.clear();
		add(new GameDrawBitmaps(this.gameDraw)
				.setYX(y, x)
				.setBitmaps(SparksImages.bitmapsAttack)
				.animateRepeat(1));
		this.frameUpdateTargetCell = this.frameCount;
		add(new GameDrawBitmaps(this.gameDraw)
				.setYX(y, x)
				.setBitmaps(SparksImages.bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1)
				.increaseFrameStart(this.frameCount));
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (this.gameDraw.iFrame == this.frameUpdateTargetCell)
		{
			this.gameDraw.gameDrawCells.updateOneCell(this.gameDraw.game, this.targetI, this.targetJ);
			this.gameDraw.gameDrawCellDual.updateOneCell(this.gameDraw.game, this.targetI, this.targetJ);
		}
	}
	
}
