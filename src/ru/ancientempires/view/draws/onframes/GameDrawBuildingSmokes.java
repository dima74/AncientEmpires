package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawBuildingSmokes extends GameDraw
{
	
	private GameDrawOnFrames[][] field = new GameDrawOnFrames[GameDraw.game.h][GameDraw.game.w];
	
	@Override
	public boolean update()
	{
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
				if (CellImages.isCellSmokes(GameDraw.game.fieldCells[i][j]))
				{
					int startY = (int) (i * GameDraw.A - GameDraw.a * 2);
					int startX = (j + 1) * GameDraw.A - SmokeImages.wSmall;
					int endY = (int) (i * GameDraw.A - GameDraw.a * 21);
					int endX = (j + 1) * GameDraw.A - SmokeImages.wSmall;
					field[i][j] = new GameDrawBitmapsMoving()
							.setLineYX(startY, startX, endY, endX)
							.setBitmaps(SmokeImages.bitmapsSmall)
							.setFramesForBitmap(10)
							.animateRepeat(1);
				}
				else
					field[i][j] = null;
		return false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (GameDrawOnFrames[] line : field)
			for (GameDrawOnFrames draw : line)
				if (draw != null)
				{
					draw.draw(canvas);
					if (GameDraw.iFrame % 2 == 0 && draw.isEndDrawing && GameDraw.main.rnd.nextInt() % 8 == 0)
						draw.reAnimate();
				}
	}
	
}
