package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.model.Cell;

public class DrawCells extends Draw
{
	
	private final int	h		= game.h;
	private final int	w		= game.w;
	// private final int availableY;
	// private final int availableX;
	
	public Bitmap[][]	bitmaps	= new Bitmap[h][w];
	private boolean		isDual	= false;
								
	public DrawCells()
	{
		// availableY = h - main.info.h;
		// availableX = w;
	}
	
	public DrawCells setDual()
	{
		isDual = true;
		return this;
	}
	
	public void update()
	{
		for (int i = h - 1 - (isDual ? 1 : 0); i >= 0; i--)
			for (int j = w - 1; j >= 0; j--)
			{
				Cell cell = game.fieldCells[i + (isDual ? 1 : 0)][j];
				bitmaps[i][j] = CellImages().getCellBitmap(cell, isDual);
			}
	}
	
	public void updateOneCell(int i, int j)
	{
		Cell cell = game.fieldCells[i + (isDual ? 1 : 0)][j];
		bitmaps[i][j] = CellImages().getCellBitmap(cell, isDual);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		/*
		int minI = -(this.gameDraw.offsetY - this.gameDraw.maxOffsetY) / A;
		int minJ = -(this.gameDraw.offsetX - this.gameDraw.maxOffsetX) / A;
		int maxI = Math.min((-(this.gameDraw.offsetY - this.gameDraw.maxOffsetY) + this.availableY + A - 1) / A, this.h);
		int maxJ = Math.min((-(this.gameDraw.offsetX - this.gameDraw.maxOffsetX) + this.availableX + A - 1) / A, this.w);
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
				final int y = A * i;
				final int x = A * j;
				canvas.drawBitmap(bitmapCell, x, y, null);
			}
	}
	
}
