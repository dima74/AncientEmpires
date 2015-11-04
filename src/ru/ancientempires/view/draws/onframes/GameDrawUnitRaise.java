package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawUnitRaise extends GameDrawOnFramesGroup
{
	
	private static final int FRAME_LENGTH = SparksImages.amountDefault * 3 * 2;
	
	private int	targetI;
	private int	targetJ;
	
	private int frameUpdateTargetUnit = -1;
	
	public GameDrawUnitRaise(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		int y = targetI * GameDraw.A - SparksImages.hDefault / 2;
		int x = targetJ * GameDraw.A - SparksImages.wDefault / 2;
		int d = (int) (4 * GameDraw.a);
		int[] array =
		{
				d, GameDraw.A - d
		};
		
		this.draws.clear();
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++)
				add(new GameDrawBitmapsMoving(this.gameDraw)
						.setLineYX(y + array[i], x + array[j], y + GameDraw.A - array[i], x + GameDraw.A - array[j])
						.setBitmaps(SparksImages.bitmapsDefault)
						.animateRepeat(3));
		this.gameDraw.gameDrawUnits.keep[targetI][targetJ] = true;
		this.frameUpdateTargetUnit = this.frameStart + GameDrawUnitRaise.FRAME_LENGTH / 2;
		this.gameDraw.gameDrawUnitsDead.keep[this.targetI][this.targetJ] = true;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (this.gameDraw.iFrame == this.frameUpdateTargetUnit)
		{
			this.gameDraw.gameDrawUnitsDead.keep[this.targetI][this.targetJ] = false;
			this.gameDraw.gameDrawUnits.keep[this.targetI][this.targetJ] = false;
		}
	}
	
}
