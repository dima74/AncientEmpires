package ru.ancientempires.view;

import java.io.IOException;

import ru.ancientempires.Cursor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameViewCursor extends GameViewPart
{
	
	public GameViewCursor(Context context, GameView gameView)
	{
		super(context, gameView);
	}
	
	private static Cursor	cursor	= new Cursor();
	
	public static void init() throws IOException
	{
		GameViewCursor.cursor.setBitmaps(new String[]
		{
				"cursor_down.png",
				"cursor_up.png"
		});
	}
	
	@Override
	public boolean update()
	{
		GameViewCursor.cursor.i = this.gameView.lastTapI;
		GameViewCursor.cursor.j = this.gameView.lastTapJ;
		GameViewCursor.cursor.isVisible = true;
		invalidate();
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// рисуем курсор (если есть)
		canvas.translate(this.gameView.offsetX, this.gameView.offsetY);
		if (GameViewCursor.cursor.isVisible)
		{
			final Bitmap bitmapCursor = GameViewCursor.cursor.getBitmap();
			final int y = GameView.baseH * GameViewCursor.cursor.i - (bitmapCursor.getHeight() - GameView.baseH) / 2;// + this.offsetY;
			final int x = GameView.baseW * GameViewCursor.cursor.j - (bitmapCursor.getWidth() - GameView.baseW) / 2;// + this.offsetX;
			canvas.drawBitmap(bitmapCursor, x, y, null);
		}
	}
	
}
