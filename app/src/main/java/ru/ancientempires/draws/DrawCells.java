package ru.ancientempires.draws;

import android.graphics.Canvas;

import ru.ancientempires.images.bitmaps.FewBitmaps;

public class DrawCells extends Draw
{
	
	// private final int h = game.h;
	// private final int w = game.w;
	// private final int availableY;
	// private final int availableX;
	
	private boolean        isDual = false;
	public  FewBitmaps[][] field  = new FewBitmaps[game.h][game.w];
	public  boolean[][]    keep   = new boolean[game.h][game.w];

	public DrawCells(BaseDrawMain mainBase)
	{
		super(mainBase);
	}
	
	public DrawCells setDual()
	{
		isDual = true;
		return this;
	}
	
	public void update()
	{
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
				updateCell(i, j);
	}
	
	public void updateCell(int i, int j)
	{
		if (!keep[i][j])
			field[i][j] = CellImages().getCellBitmap(game.fieldCells[i][j], isDual);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		/*
		int minI = -(this.gameDraw.offsetY - this.gameDraw.maxOffsetY) / A;
		int minJ = -(this.gameDraw.offsetX - this.gameDraw.maxOffsetX) / A;
		int maxI = Math.min((-(this.gameDraw.offsetY - this.gameDraw.maxOffsetY) + this.availableY + A - 1) / A, this.h);
		int maxJ = Math.min((-(this.gameDraw.offsetX - this.gameDraw.maxOffsetX) + this.availableX + A - 1) / A, this.w);
		int minI = 0;
		int minJ = 0;
		int maxI = game.h;
		int maxJ = game.w;
		*/
		for (int i = mainBase.iMin; i < mainBase.iMax; i++)
			for (int j = mainBase.jMin; j < mainBase.jMax; j++)
			{
				updateCell(i, j);
				FewBitmaps bitmap = field[i][j];
				if (bitmap == null)
					continue;
				final int y = A * i - (isDual ? A : 0);
				final int x = A * j;
				canvas.drawBitmap(bitmap.getBitmap(), x, y, null);
			}
	}
	
}
