package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.model.Game;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawBuildingSmokes extends GameDraw
{
	
	private GameDrawOnFrames[][] field;
	
	public GameDrawBuildingSmokes(GameDrawMain gameDraw)
	{
		super(gameDraw);
		
		this.field = new GameDrawOnFrames[gameDraw.game.h][gameDraw.game.w];
		update(gameDraw.game);
	}
	
	@Override
	public boolean update(Game game)
	{
		for (int i = 0; i < this.field.length; i++)
			for (int j = 0; j < this.field[i].length; j++)
				if (CellImages.isCellSmokes(game.fieldCells[i][j]))
				{
					int startY = (int) (i * GameDraw.A - GameDraw.a * 2);
					int startX = (j + 1) * GameDraw.A - SmokeImages.wSmall;
					int endY = (int) (i * GameDraw.A - GameDraw.a * 21);
					int endX = (j + 1) * GameDraw.A - SmokeImages.wSmall;
					this.field[i][j] = new GameDrawBitmapsMoving(this.gameDraw)
							.setLineYX(startY, startX, endY, endX)
							.setBitmaps(SmokeImages.bitmapsSmall)
							.setFramesForBitmap(10)
							.animateRepeat(1);
				}
				else
					this.field[i][j] = null;
		return false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (GameDrawOnFrames[] line : this.field)
			for (GameDrawOnFrames draw : line)
				if (draw != null)
				{
					draw.draw(canvas);
					if (this.gameDraw.iFrame % 2 == 0 && draw.isEndDrawing && this.gameDraw.rnd.nextInt() % 8 == 0)
						draw.reAnimate();
				}
	}
	
}
