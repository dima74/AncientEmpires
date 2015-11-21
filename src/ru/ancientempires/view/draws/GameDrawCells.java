package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.GameView;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;

public class GameDrawCells extends GameDraw
{
	
	private final int	h	= GameDraw.game.h;
	private final int	w	= GameDraw.game.w;
	private final int	availableY;
	private final int	availableX;
	
	public Bitmap[][]	bitmaps	= new Bitmap[h][w];
	private boolean		isDual	= false;
	
	public GameDrawCells()
	{
		availableY = GameView.h - GameDraw.main.gameDrawInfo.h;
		availableX = GameView.w;
	}
	
	public GameDrawCells setDual()
	{
		isDual = true;
		return this;
	}
	
	@Override
	public boolean update()
	{
		for (int i = h - 1 - (isDual ? 1 : 0); i >= 0; i--)
			for (int j = w - 1; j >= 0; j--)
			{
				Cell cell = GameDraw.game.fieldCells[i + (isDual ? 1 : 0)][j];
				bitmaps[i][j] = CellImages.getCellBitmap(cell, isDual);
			}
		return false;
	}
	
	public void updateOneCell(int i, int j)
	{
		Cell cell = GameDraw.game.fieldCells[i + (isDual ? 1 : 0)][j];
		bitmaps[i][j] = CellImages.getCellBitmap(cell, isDual);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		/*
		int minI = -(this.gameDraw.offsetY - this.gameDraw.maxOffsetY) / GameDraw.A;
		int minJ = -(this.gameDraw.offsetX - this.gameDraw.maxOffsetX) / GameDraw.A;
		int maxI = Math.min((-(this.gameDraw.offsetY - this.gameDraw.maxOffsetY) + this.availableY + GameDraw.A - 1) / GameDraw.A, this.h);
		int maxJ = Math.min((-(this.gameDraw.offsetX - this.gameDraw.maxOffsetX) + this.availableX + GameDraw.A - 1) / GameDraw.A, this.w);
		*/
		int minI = 0;
		int minJ = 0;
		int maxI = h;
		int maxJ = w;
		for (int i = minI; i < maxI; i++)
			for (int j = minJ; j < maxJ; j++)
			{
				final Bitmap bitmapCell = bitmaps[i][j];
				if (bitmapCell == null)
					continue;
				final int y = GameDraw.A * i;
				final int x = GameDraw.A * j;
				canvas.drawBitmap(bitmapCell, x, y, null);
			}
	}
	
}
