package ru.ancientempires.editor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.MyColor;
import ru.ancientempires.Paints;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;

public abstract class EditorStruct
{
	
	public static int getNearest(EditorStruct[] structs, float touchY, float touchX)
	{
		int nearest = 0;
		for (int i = 1; i < structs.length; i++)
			if (getDistance(structs[i], touchY, touchX) < getDistance(structs[nearest], touchY, touchX))
				nearest = i;
		return nearest;
	}
	
	private static double getDistance(EditorStruct struct, float touchY, float touchX)
	{
		return Math.hypot(struct.y - touchY, struct.x - touchX);
	}
	
	public float	y;
	public float	x;
	public Game		game;
					
	public EditorStruct()
	{}
	
	public EditorStruct(Game game)
	{
		this.game = game;
	}
	
	public EditorStruct setYX(float y, float x)
	{
		this.y = y;
		this.x = x;
		return this;
	}
	
	public Player getPlayer(MyColor color)
	{
		for (Player player : game.players)
			if (player.color == color)
				return player;
		return null;
	}
	
	public abstract void setColor(MyColor color);
	
	public abstract void drawBitmap(Canvas canvas);
	
	public void drawBitmap(Canvas canvas, Bitmap bitmap)
	{
		drawBitmap(canvas, bitmap, x, y);
	}
	
	public void drawBitmap(Canvas canvas, Bitmap bitmap, float x, float y)
	{
		canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, Paints.ANTI_ALIAS_FLAG);
	}
	
	public abstract EditorStruct createCopy();
	
}
