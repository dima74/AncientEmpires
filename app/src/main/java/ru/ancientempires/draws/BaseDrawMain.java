package ru.ancientempires.draws;

import android.graphics.Canvas;
import android.graphics.Color;

import ru.ancientempires.draws.onframes.DrawBuildingSmokes;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.bitmaps.FewBitmaps;

public abstract class BaseDrawMain extends Draw
{
	
	{
		setupStatic();
	}
	
	public void setupStatic()
	{}
	
	public int mapH;
	public int mapW;
	public int visibleMapH;
	public int visibleMapW;
	public int iFrame;

	volatile public float nextOffsetY;
	volatile public float nextOffsetX;
	public          float offsetY;
	public          float offsetX;
	public          float extraOffsetY;
	public          float extraOffsetX;

	public int iMin;
	public int iMax;
	public int jMin;
	public int jMax;

	public float maxOffsetY;
	public float maxOffsetX;
	public float minOffsetY;
	public float minOffsetX;

	public DrawCells          cells          = new DrawCells();
	public DrawCells          cellsDual      = new DrawCells().setDual();
	public DrawUnitsDead      unitsDead      = new DrawUnitsDead();
	public DrawUnits          units          = new DrawUnits();
	public DrawBuildingSmokes buildingSmokes = new DrawBuildingSmokes();

	public abstract void setVisibleMapSize();
	
	public final void initOffset()
	{
		setVisibleMapSize();
		mapH = game.h * A;
		mapW = game.w * A;
		offsetY = nextOffsetY = minOffsetY = maxOffsetY = -(mapH - visibleMapH / mapScale) / 2;
		offsetX = nextOffsetX = minOffsetX = maxOffsetX = -(mapW - visibleMapW / mapScale) / 2;
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
		setBounds();
		canvas.drawColor(Color.WHITE);
		
		canvas.save();
		canvas.scale(mapScale, mapScale);
		canvas.translate(offsetX, offsetY);

		canvas.translate(extraOffsetX, extraOffsetY);
		cells.draw(canvas);
		cellsDual.draw(canvas);
		canvas.translate(-extraOffsetX, -extraOffsetY);
		unitsDead.draw(canvas);
		units.draw(canvas);
	}

	public void setBounds()
	{
		for (int i = 0; i < game.h; i++)
			if (i * A <= -offsetY)
				iMin = i;
		for (int i = game.h; i >= 0; i--)
			if (i * A >= -offsetY + visibleMapH / mapScale)
				iMax = i;
		for (int j = 0; j < game.w; j++)
			if (j * A <= -offsetX)
				jMin = j;
		for (int j = game.w; j >= 0; j--)
			if (j * A >= -offsetX + visibleMapW / mapScale)
				jMax = j;
		iMin = Math.max(0, iMin - 1);
		iMax = Math.min(game.h, iMax + 1);
		jMin = Math.max(0, jMin - 1);
		jMax = Math.min(game.w, jMax + 1);
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
