package ru.ancientempires.draws;

import android.graphics.Canvas;
import android.graphics.Color;

import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.draws.onframes.DrawBuildingSmokes;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.load.GamePath;

public class BaseDrawMain extends Draw
{

	public BaseGameActivity activity;

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

	public DrawCells          cells;
	public DrawCells          cellsDual;
	public DrawUnitsDead      unitsDead;
	public DrawUnits          units;
	public DrawBuildingSmokes buildingSmokes;

	public BaseDrawMain(BaseGameActivity activity)
	{
		super(null);
		mainBase = this;
		this.activity = activity;
		game = activity.game;
		activity.drawMain = this;
		cells = new DrawCells(this);
		cellsDual = new DrawCells(this).setDual();
		unitsDead = new DrawUnitsDead(this);
		units = new DrawUnits(this);
		buildingSmokes = new DrawBuildingSmokes(this);
	}

	public void setVisibleMapSize() {}

	public final void initOffset()
	{
		setVisibleMapSize();
		mapH = game.h * A;
		mapW = game.w * A;
		minOffsetY = maxOffsetY = -(mapH - visibleMapH / mapScale) / 2;
		minOffsetX = maxOffsetX = -(mapW - visibleMapW / mapScale) / 2;
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
		iMin = (int) (-offsetY / A);
		iMax = game.h - (int) ((mapH + offsetY - visibleMapH) / A);
		iMin = (int) (-offsetX / A);
		jMax = game.w - (int) ((mapW + offsetX - visibleMapW) / A);
		iMin = Math.max(iMin, 0);
		jMin = Math.max(jMin, 0);
		iMax = Math.min(iMax, game.h);
		jMax = Math.min(jMax, game.w);
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
		nextOffsetY = Math.max(minOffsetY, Math.min(maxOffsetY, offsetY));
		nextOffsetX = Math.max(minOffsetX, Math.min(maxOffsetX, offsetX));
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

	public void focusOnCell(float i, float j)
	{
		float nextOffsetY = -i * A - A / 2 + visibleMapH / mapScale / 2;
		float nextOffsetX = -j * A - A / 2 + visibleMapW / mapScale / 2;
		setNextOffset(nextOffsetY, nextOffsetX);
	}

	public void focusOnCurrentPlayerCenter()
	{
		focusOn(game.path.screenCenters[game.currentPlayer.ordinal]);
	}

	public void focusOn(GamePath.PointScreenCenter point)
	{
		float nextOffsetY = -point.i * A + visibleMapH / mapScale / 2 - maxOffsetY;
		float nextOffsetX = -point.j * A + visibleMapW / mapScale / 2 - maxOffsetX;
		setNextOffset(nextOffsetY, nextOffsetX);
	}

	public void saveScreenCenter()
	{
		game.path.screenCenters[game.currentPlayer.ordinal] = getScreenCenter();
	}

	public GamePath.PointScreenCenter getScreenCenter()
	{
		float i = (-offsetY + visibleMapH / mapScale / 2 - maxOffsetY) / A;
		float j = (-offsetX + visibleMapW / mapScale / 2 - maxOffsetX) / A;
		return new GamePath.PointScreenCenter(i, j);
	}

}
