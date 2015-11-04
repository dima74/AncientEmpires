package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.view.GameView;

public class GameDrawCells extends GameDraw
{
	
	private final int	h;
	private final int	w;
	private final int	availableY;
	private final int	availableX;
	
	public Bitmap[][]	bitmaps;
	private boolean		isDual	= false;
	
	public GameDrawCells(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.h = gameDraw.game.h;
		this.w = gameDraw.game.w;
		this.bitmaps = new Bitmap[this.h][this.w];
		
		this.availableY = GameView.h - gameDraw.gameDrawInfoH;
		this.availableX = GameView.w;
	}
	
	public GameDrawCells setDual()
	{
		this.isDual = true;
		return this;
	}
	
	@Override
	public boolean update(Game game)
	{
		for (int i = this.h - 1 - (this.isDual ? 1 : 0); i >= 0; i--)
			for (int j = this.w - 1; j >= 0; j--)
			{
				Cell cell = game.fieldCells[i + (this.isDual ? 1 : 0)][j];
				this.bitmaps[i][j] = CellImages.getCellBitmap(cell, this.isDual);
			}
		return false;
	}
	
	public void updateOneCell(Game game, int i, int j)
	{
		Cell cell = game.fieldCells[i + (this.isDual ? 1 : 0)][j];
		this.bitmaps[i][j] = CellImages.getCellBitmap(cell, this.isDual);
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
		int maxI = this.h;
		int maxJ = this.w;
		for (int i = minI; i < maxI; i++)
			for (int j = minJ; j < maxJ; j++)
			{
				final Bitmap bitmapCell = this.bitmaps[i][j];
				if (bitmapCell == null)
					continue;
				final int y = GameDraw.A * i;
				final int x = GameDraw.A * j;
				canvas.drawBitmap(bitmapCell, x, y, null);
			}
	}
	
}
