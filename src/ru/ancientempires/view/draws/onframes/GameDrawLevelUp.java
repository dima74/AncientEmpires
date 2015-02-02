package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.images.Images;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawBitmapsMoving;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawLevelUp extends GameDrawOnFramesGroup
{
	
	// 2 2 2 2 40
	// 21 17 13 9 5
	
	private static final int	FRAME_ANIMATE			= 48;
	private static final int	FRAME_ANIMATE_MOTION	= 16;
	private static final int	FRAME_ANIMATE_STATIC	= 32;
	
	public GameDrawLevelUp(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void animate(int frameToStart, int y, int x)
	{
		super.animate(frameToStart, GameDrawLevelUp.FRAME_ANIMATE);
		
		int levelUpX = x + (GameDraw.A - Images.levelUpW) / 2;
		int levelUpYStart = (int) (y - GameDraw.A + 20 * GameDraw.a);
		int levelUpYEnd = (int) (y - GameDraw.A + 5 * GameDraw.a);
		
		int amountLevelUps = 4;
		for (int i = amountLevelUps; i >= 0; i--)
			this.draws.add(new GameDrawBitmapsMoving(this.gameDraw).animate(
					frameToStart + (int) (i * 3 * GameDraw.a), levelUpYStart, levelUpX, levelUpYEnd, levelUpX, GameDrawLevelUp.FRAME_ANIMATE_MOTION)
					.setBitmaps(new Bitmap[]
					{
							Images.levelUp
					}));
		
		this.draws.add(new GameDrawBitmaps(this.gameDraw).setBitmaps(new Bitmap[]
		{
				Images.levelUp
		}).animate(frameToStart + GameDrawLevelUp.FRAME_ANIMATE_MOTION, levelUpYEnd, levelUpX, GameDrawLevelUp.FRAME_ANIMATE_STATIC));
		
		this.frameStart = this.gameDraw.iFrame + frameToStart;
		this.frameEnd = 0;
		for (GameDrawOnFrames gameDrawOnFrames : this.draws)
			this.frameEnd = Math.max(this.frameEnd, gameDrawOnFrames.frameEnd);
		this.frameLength = this.frameEnd - this.frameStart;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
	}
	
}
