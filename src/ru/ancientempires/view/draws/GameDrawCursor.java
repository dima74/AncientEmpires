package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.images.bitmaps.FewBitmaps;

public class GameDrawCursor extends GameDraw
{
	
	private FewBitmaps	cursor;
	private int			cursorH;
	private int			cursorW;
	
	public boolean	isDrawing	= true;
	private int		cursorY;
	private int		cursorX;
	
	public GameDrawCursor setCursor(FewBitmaps cursor)
	{
		this.cursor = cursor;
		cursorH = cursor.getBitmap().getHeight();
		cursorW = cursor.getBitmap().getWidth();
		return this;
	}
	
	@Override
	public boolean update()
	{
		int y = GameDraw.A * GameDraw.game.currentPlayer.cursorI;
		int x = GameDraw.A * GameDraw.game.currentPlayer.cursorJ;
		cursorY = y - cursorH / 2;
		cursorX = x - cursorW / 2;
		return false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (!isDrawing)
			return;
			
		canvas.translate(GameDraw.A / 2, GameDraw.A / 2);
		canvas.drawBitmap(cursor.getBitmap(), cursorX, cursorY, null);
		canvas.translate(-GameDraw.A / 2, -GameDraw.A / 2);
	}
	
}
