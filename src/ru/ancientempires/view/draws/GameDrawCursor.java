package ru.ancientempires.view.draws;

import ru.ancientempires.images.SomeWithBitmaps;
import ru.ancientempires.model.Game;
import android.graphics.Canvas;

public class GameDrawCursor extends GameDraw
{
	
	public GameDrawCursor(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	private SomeWithBitmaps	cursor;
	private int				cursorH;
	private int				cursorW;
	
	public boolean			isDrawing	= true;
	private int				cursorY;
	private int				cursorX;
	
	public GameDrawCursor setCursor(SomeWithBitmaps cursor)
	{
		this.cursor = cursor;
		this.cursorH = cursor.getBitmap().getHeight();
		this.cursorW = cursor.getBitmap().getWidth();
		return this;
	}
	
	@Override
	public boolean update(Game game)
	{
		int y = GameDraw.A * game.currentPlayer.cursorI;
		int x = GameDraw.A * game.currentPlayer.cursorJ;
		this.cursorY = y - this.cursorH / 2;
		this.cursorX = x - this.cursorW / 2;
		return false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (!this.isDrawing)
			return;
		
		canvas.translate(GameDraw.A / 2, GameDraw.A / 2);
		canvas.drawBitmap(this.cursor.getBitmap(), this.cursorX, this.cursorY, null);
		canvas.translate(-GameDraw.A / 2, -GameDraw.A / 2);
	}
	
}
