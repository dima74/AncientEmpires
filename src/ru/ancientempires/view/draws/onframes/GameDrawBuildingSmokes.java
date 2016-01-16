package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawBuildingSmokes extends GameDraw
{
	
	private GameDrawOnFrames[][] field = new GameDrawOnFrames[game.h][game.w];
	
	@Override
	public void update()
	{
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
				if (CellImages().isCellSmokes(game.fieldCells[i][j]))
				{
					int startY = (int) (i * A - a * 2);
					int startX = (j + 1) * A - SmokeImages().wSmall;
					int endY = (int) (i * A - a * 21);
					int endX = (j + 1) * A - SmokeImages().wSmall;
					field[i][j] = new GameDrawBitmapsMoving()
							.setLineYX(startY, startX, endY, endX)
							.setBitmaps(SmokeImages().bitmapsSmall)
							.setFramesForBitmap(10)
							.animateRepeat(1);
				}
				else
					field[i][j] = null;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (GameDrawOnFrames[] line : field)
			for (GameDrawOnFrames draw : line)
				if (draw != null)
				{
					draw.draw(canvas);
					if (iFrame() % 2 == 0 && draw.isEndDrawing && main.rnd.nextInt() % 8 == 0)
						draw.reAnimate();
				}
	}
	
}
