package ru.ancientempires.draws;

import android.graphics.Canvas;

import ru.ancientempires.images.bitmaps.FewBitmaps;

public class DrawCursor extends Draw
{
	
	private FewBitmaps cursor;
	private int        cursorH;
	private int        cursorW;
	
	private int cursorY;
	private int cursorX;
	
	public DrawCursor setCursor(FewBitmaps cursor)
	{
		this.cursor = cursor;
		cursorH = cursor.getBitmap().getHeight();
		cursorW = cursor.getBitmap().getWidth();
		return this;
	}
	
	public void update()
	{
		int y = A * game.currentPlayer.cursorI;
		int x = A * game.currentPlayer.cursorJ;
		cursorY = y + (A - cursorH) / 2;
		cursorX = x + (A - cursorW) / 2;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(cursor.getBitmap(), cursorX, cursorY, null);
	}
	
}
