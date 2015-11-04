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
	
	protected GameDrawMain gameDraw;
	
	public GameDraw(GameDrawMain gameDraw)
	{
		this.gameDraw = gameDraw;
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{}
	
	public boolean update(Game game)
	{
		return false;
	}
	
	public void draw(Canvas canvas)
	{}
	
}
