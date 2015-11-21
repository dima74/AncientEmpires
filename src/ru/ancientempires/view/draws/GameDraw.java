package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.images.Images;
import ru.ancientempires.model.Game;

public abstract class GameDraw
{
	
	public final static float	mapScale	= 2;
	public final static int		A			= Images.bitmapSize;
	public final static float	fA			= Images.bitmapSize * GameDraw.mapScale;
	public final static float	a			= Images.bitmapSize / 24.0f;			// 1/24 A
	
	public static GameDrawMain	main;
	public static Game			game;
	public static int			iFrame;
	
	public boolean update()
	{
		return false;
	}
	
	public void draw(Canvas canvas)
	{}
	
}
