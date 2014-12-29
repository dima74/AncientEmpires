package ru.ancientempires.view.draws;

import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawCell extends GameDraw
{
	
	private final int	h;
	private final int	w;
	
	public GameDrawCell(GameDrawMain gameDraw)
	{
		super(gameDraw);
		Cell[][] field = gameDraw.game.map.getField();
		this.h = field.length;
		this.w = field[0].length;
		this.bitmaps = new Bitmap[this.h][this.w];
	}
	
	public Bitmap[][]	bitmaps;
	private boolean		isDual;
	
	public GameDrawCell setDual()
	{
		this.isDual = true;
		return this;
	}
	
	@Override
	public boolean update(Game game)
	{
		final Cell[][] field = game.map.getField();
		for (int i = this.h - 1 - (this.isDual ? 1 : 0); i >= 0; i--)
			for (int j = this.w - 1; j >= 0; j--)
			{
				Cell cell = field[i + (this.isDual ? 1 : 0)][j];
				this.bitmaps[i][j] = CellImages.getCellBitmap(cell, this.isDual);
			}
		return false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (int i = 0; i < this.h; i++)
			for (int j = 0; j < this.w; j++)
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
