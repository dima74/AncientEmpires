package ru.ancientempires.view.draws;

import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.view.GameView;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawCell extends GameDraw
{
	
	private final int	h;
	private final int	w;
	private final int	availableY;
	private final int	availableX;
	
	public Bitmap[][]	bitmaps;
	private boolean		isDual	= false;
	
	public GameDrawCell(GameDrawMain gameDraw)
	{
		super(gameDraw);
		Cell[][] field = gameDraw.game.map.getField();
		this.h = field.length;
		this.w = field[0].length;
		this.bitmaps = new Bitmap[this.h][this.w];
		
		this.availableY = GameView.h - gameDraw.gameDrawInfoH;
		this.availableX = GameView.w;
	}
	
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
	
	public void updateOneCell(Game game, int i, int j)
	{
		final Cell[][] field = game.map.getField();
		Cell cell = field[i + (this.isDual ? 1 : 0)][j];
		this.bitmaps[i][j] = CellImages.getCellBitmap(cell, this.isDual);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		int minI = -(this.gameDraw.offsetY - this.gameDraw.minOffsetY) / GameDraw.A;
		int minJ = -(this.gameDraw.offsetX - this.gameDraw.minOffsetX) / GameDraw.A;
		int maxI = Math.min((-(this.gameDraw.offsetY - this.gameDraw.minOffsetY) + this.availableY + GameDraw.A - 1) / GameDraw.A, this.h);
		int maxJ = Math.min((-(this.gameDraw.offsetX - this.gameDraw.minOffsetX) + this.availableX + GameDraw.A - 1) / GameDraw.A, this.w);
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
