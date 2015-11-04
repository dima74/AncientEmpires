package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.images.Images;

public class GameDrawUnitsDead extends GameDraw
{
	
	public boolean[][]	isTombstone;
	public boolean[][]	keep;
	
	public GameDrawUnitsDead(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.isTombstone = new boolean[GameHandler.h][GameHandler.w];
		this.keep = new boolean[GameHandler.h][GameHandler.w];
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (int i = 0; i < GameHandler.h; i++)
			for (int j = 0; j < GameHandler.w; j++)
			{
				if (!this.keep[i][j])
					this.isTombstone[i][j] = GameHandler.fieldDeadUnits[i][j] != null;
				if (this.isTombstone[i][j])
					canvas.drawBitmap(Images.tombstone.getBitmap(), j * GameDraw.A, i * GameDraw.A, null);
			}
	}
	
}
