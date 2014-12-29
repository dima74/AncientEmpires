package ru.ancientempires.view.draws;

import ru.ancientempires.images.Images;
import ru.ancientempires.model.Game;
import android.graphics.Canvas;

public abstract class GameDraw
{
	
	public final static int		A	= Images.bitmapSize;
	public final static float	a	= Images.bitmapSize / 24.0f;	// 1/24 A
																	
	protected GameDrawMain		gameDraw;
	
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
