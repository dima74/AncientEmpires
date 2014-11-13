package ru.ancientempires.view;

import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameViewCell extends GameViewPart
{
	
	public GameViewCell(Context context, GameView gameView)
	{
		super(context, gameView);
	}
	
	private Cell[][]	field;
	private int			h;
	private int			w;
	
	public GameViewCell setField(Cell[][] field)
	{
		this.field = field;
		this.h = field.length;
		this.w = field[0].length;
		
		this.bitmaps = new Bitmap[this.h][this.w];
		update();
		
		return this;
	}
	
	private Bitmap[][]	bitmaps;
	private boolean		isDual;
	
	public GameViewCell setDual(boolean isDual)
	{
		this.isDual = isDual;
		return this;
	}
	
	@Override
	public boolean update()
	{
		for (int i = this.h - 1 - (this.isDual ? 1 : 0); i >= 0; i--)
			for (int j = this.w - 1; j >= 0; j--)
			{
				Cell cell = this.isDual ? this.field[i + 1][j] : this.field[i][j];
				this.bitmaps[i][j] = CellImages.getCellBitmap(cell, this.isDual);
			}
		invalidate();
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.translate(this.gameView.offsetX, this.gameView.offsetY);
		// карта
		for (int i = 0; i < this.h; i++)
			for (int j = 0; j < this.w; j++)
			{
				final Bitmap bitmapCell = this.bitmaps[i][j];
				if (bitmapCell == null)
					continue;
				final int y = GameView.baseH * i;
				final int x = GameView.baseW * j;
				canvas.drawBitmap(bitmapCell, x, y, null);
			}
	}
	
}
