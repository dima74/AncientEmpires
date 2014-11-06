package ru.ancientempires.view;

import ru.ancientempires.CellBitmap;
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
	private int			height;
	private int			width;
	
	public GameViewCell setField(Cell[][] field)
	{
		this.field = field;
		this.height = field.length;
		this.width = field[0].length;
		
		this.bitmaps = new Bitmap[this.height][this.width];
		this.offsetI = new int[this.height][this.width];
		this.offsetJ = new int[this.height][this.width];
		update();
		
		return this;
	}
	
	protected Bitmap[][]	bitmaps;
	protected int[][]		offsetI, offsetJ;
	
	@Override
	public boolean update()
	{
		for (int i = 0; i < this.height; i++)
			for (int j = 0; j < this.width; j++)
			{
				Cell cell = this.field[i][j];
				CellBitmap cellBitmap = CellImages.cellsBitmaps[cell.type.ordinal];
				this.bitmaps[i][j] = cellBitmap.getBitmap(cell);
				this.offsetI[i][j] = cellBitmap.offsetI;
				this.offsetJ[i][j] = cellBitmap.offsetJ;
			}
		invalidate();
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.translate(this.gameView.offsetX, this.gameView.offsetY);
		// карта
		for (int i = 0; i < this.height; i++)
			for (int j = 0; j < this.width; j++)
			{
				final Bitmap bitmapCell = this.bitmaps[i][j];
				final int y = GameView.baseH * (i + this.offsetI[i][j]);
				final int x = GameView.baseW * (j + this.offsetJ[i][j]);
				canvas.drawBitmap(bitmapCell, x, y, null);
			}
	}
	
}
