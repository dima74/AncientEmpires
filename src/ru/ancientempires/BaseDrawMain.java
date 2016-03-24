package ru.ancientempires;

import android.graphics.Canvas;
import android.graphics.Color;
import ru.ancientempires.draws.Draw;
import ru.ancientempires.draws.DrawCells;
import ru.ancientempires.draws.DrawUnits;
import ru.ancientempires.draws.DrawUnitsDead;
import ru.ancientempires.draws.onframes.DrawBuildingSmokes;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.bitmaps.FewBitmaps;

public abstract class BaseDrawMain extends Draw
{
	
	public GameView				view;
								
	public int					mapH;
	public int					mapW;
	public int					visibleMapH;
	public int					visibleMapW;
	public int					iFrame;
								
	volatile public float		nextOffsetY;
	volatile public float		nextOffsetX;
	public float				offsetY;
	public float				offsetX;
								
	public float				maxOffsetY;
	public float				maxOffsetX;
	public float				minOffsetY;
	public float				minOffsetX;
								
	public DrawCells			cells			= new DrawCells();
	public DrawCells			cellsDual		= new DrawCells().setDual();
	public DrawUnitsDead		unitsDead		= new DrawUnitsDead();
	public DrawUnits			units			= new DrawUnits();
	public DrawBuildingSmokes	buildingSmokes	= new DrawBuildingSmokes();
												
	public abstract void setVisibleMapSize();
	
	public final void initOffset()
	{
		setVisibleMapSize();
		mapH = game.h * A;
		mapW = game.w * A;
		nextOffsetY = minOffsetY = maxOffsetY = -(mapH - visibleMapH / mapScale) / 2;
		nextOffsetX = minOffsetX = maxOffsetX = -(mapW - visibleMapW / mapScale) / 2;
		if (minOffsetY < 0)
		{
			minOffsetY = -(mapH - visibleMapH / mapScale);
			maxOffsetY = 0;
		}
		if (minOffsetX < 0)
		{
			minOffsetX = -(mapW - visibleMapW / mapScale);
			maxOffsetX = 0;
		}
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		FewBitmaps.ordinal = iFrame / 8;
		synchronized (this)
		{
			offsetY = nextOffsetY;
			offsetX = nextOffsetX;
		}
		canvas.drawColor(Color.WHITE);
		
		canvas.save();
		canvas.scale(mapScale, mapScale);
		canvas.translate(offsetX, offsetY);
		
		cells.draw(canvas);
		cellsDual.draw(canvas);
		unitsDead.draw(canvas);
		units.draw(canvas);
	}
	
	public boolean isActiveGame()
	{
		return true;
	}
	
	synchronized public void onScroll(float distanceY, float distanceX)
	{
		setNextOffset(nextOffsetY - distanceY / mapScale, nextOffsetX - distanceX / mapScale);
	}
	
	synchronized public void setNextOffset(float offsetY, float offsetX)
	{
		nextOffsetY = offsetY;
		nextOffsetX = offsetX;
		nextOffsetY = Math.max(minOffsetY, Math.min(maxOffsetY, nextOffsetY));
		nextOffsetX = Math.max(minOffsetX, Math.min(maxOffsetX, nextOffsetX));
	}
	
	// Обрабатывает только тап по клеточке
	public void touch(float touchY, float touchX)
	{
		int i = Math.round((touchY / mapScale - offsetY) / A - 0.5f);
		int j = Math.round((touchX / mapScale - offsetX) / A - 0.5f);
		if (0 <= i && i < game.h && 0 <= j && j < game.w)
			try
			{
				tap(i, j);
			}
			catch (Exception e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
	}
	
	public void tap(int i, int j)
	{}
	
}
