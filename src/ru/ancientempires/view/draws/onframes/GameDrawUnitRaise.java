package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawUnitRaise extends GameDrawOnFramesGroup
{
	
	private static final int FRAME_LENGTH = SparksImages.amountDefault * 3 * 2;
	
	private int	targetI;
	private int	targetJ;
	
	private int frameUpdateTargetUnit = -1;
	
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
		
		draws.clear();
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++)
				add(new GameDrawBitmapsMoving()
						.setLineYX(y + array[i], x + array[j], y + GameDraw.A - array[i], x + GameDraw.A - array[j])
						.setBitmaps(SparksImages.bitmapsDefault)
						.animateRepeat(3));
		GameDraw.main.gameDrawUnits.keep[targetI][targetJ] = true;
		frameUpdateTargetUnit = frameStart + GameDrawUnitRaise.FRAME_LENGTH / 2;
		GameDraw.main.gameDrawUnitsDead.keep[this.targetI][this.targetJ] = true;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (GameDraw.iFrame == frameUpdateTargetUnit)
		{
			GameDraw.main.gameDrawUnitsDead.keep[targetI][targetJ] = false;
			GameDraw.main.gameDrawUnits.keep[targetI][targetJ] = false;
		}
	}
	
}
