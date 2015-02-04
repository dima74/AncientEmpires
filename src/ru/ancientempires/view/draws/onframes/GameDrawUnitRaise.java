package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.images.SparksImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawUnitRaise extends GameDrawOnFramesGroup
{
	
	private static final int	FRAME_LENGTH			= 18 * 2;
	
	private Bitmap[]			bitmaps;
	private int					targetI;
	private int					targetJ;
	
	private int					frameUpdateTargetUnit	= -1;
	
	public GameDrawUnitRaise(GameDrawMain gameDraw)
	{
		super(gameDraw);
		
		int amount = SparksImages.amountDefault;
		this.bitmaps = new Bitmap[amount * 3];
		for (int i = 0; i < amount; i++)
			this.bitmaps[i] = this.bitmaps[i + amount] = this.bitmaps[i + amount * 2] = SparksImages.bitmapsDefault[i];
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
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++)
			{
				GameDrawOnFrames gameDraw = new GameDrawBitmapsMoving(this.gameDraw)
						.animate(0, y + array[i], x + array[j], y + GameDraw.A - array[i], x + GameDraw.A - array[j], GameDrawUnitRaise.FRAME_LENGTH)
						.setBitmaps(this.bitmaps);
				this.draws.add(gameDraw);
			}
		animate(0, GameDrawUnitRaise.FRAME_LENGTH);
		this.frameUpdateTargetUnit = this.frameStart + GameDrawUnitRaise.FRAME_LENGTH / 2;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (this.gameDraw.iFrame == this.frameUpdateTargetUnit)
			this.gameDraw.gameDrawUnit.updateOneUnit(this.targetI, this.targetJ);
	}
	
}
