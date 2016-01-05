package ru.ancientempires.view.draws.onframes;

import android.graphics.Bitmap;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawLevelUp extends GameDrawOnFramesGroup
{
	
	// 2 2 2 2 40
	// 21 17 13 9 5
	
	private static final int	FRAME_ANIMATE			= 48;
	private static final int	FRAME_ANIMATE_MOTION	= 16;
	private static final int	FRAME_ANIMATE_STATIC	= 32;
	
	public GameDrawLevelUp animate(int y, int x)
	{
		int levelUpX = x + (GameDraw.A - Images().levelUpW) / 2;
		int levelUpYStart = (int) (y - GameDraw.A + 20 * GameDraw.a);
		int levelUpYEnd = (int) (y - GameDraw.A + 5 * GameDraw.a);
		
		int amountLevelUps = 4;
		for (int i = amountLevelUps; i >= 0; i--)
			add(new GameDrawBitmapsMoving()
					.setLineYX(levelUpYStart, levelUpX, levelUpYEnd, levelUpX)
					.setBitmaps(new Bitmap[]
			{
					Images().levelUp
			})
					.setFramesForBitmap(GameDrawLevelUp.FRAME_ANIMATE_MOTION)
					.animateRepeat(1)
					.increaseFrameStart((int) (i * 3 * GameDraw.a)));
		add(new GameDrawBitmap()
				.setYX(levelUpYEnd, levelUpX)
				.setBitmap(Images().levelUp)
				.animate(GameDrawLevelUp.FRAME_ANIMATE_STATIC)
				.increaseFrameStart(GameDrawLevelUp.FRAME_ANIMATE_MOTION));
		return this;
	}
	
}
