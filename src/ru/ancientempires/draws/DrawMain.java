package ru.ancientempires.draws;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import ru.ancientempires.GameView;
import ru.ancientempires.draws.inputs.InputMain;
import ru.ancientempires.draws.inputs.InputPlayer;
import ru.ancientempires.draws.onframes.DrawBlackScreen;
import ru.ancientempires.draws.onframes.DrawBuildingSmokes;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.bitmaps.FewBitmaps;

public class DrawMain extends Draw
{
	
	public static DrawMain main;
	
	{
		DrawMain.main = this;
	}
	
	public GameView		view;
	public InputMain	inputMain;
	public InputPlayer	inputPlayer;
						
	public void setInputMain(InputMain inputMain)
	{
		this.inputMain = inputMain;
		inputPlayer = inputMain.inputPlayer;
	}
	
	public Random					rnd				= new Random();
													
	public int						mapH;
	public int						mapW;
	public int						visibleMapH;
	public int						visibleMapW;
	public int						iFrame;
									
	volatile public float			nextOffsetY;
	volatile public float			nextOffsetX;
	public float					offsetY;
	public float					offsetX;
									
	private int						actionY;
	public DrawAction				action			= new DrawAction();
	public DrawInfoNull				infoNull		= new DrawInfoNull();
	public DrawInfo					info			= new DrawInfo();
	public DrawInfoMove				infoMove		= new DrawInfoMove();
	public int						infoY			= 0;
	volatile public boolean			isActiveGame	= true;
													
	public DrawCells				cells			= new DrawCells();
	public DrawCells				cellsDual		= new DrawCells().setDual();
	public DrawUnitsDead			unitsDead		= new DrawUnitsDead();
	public DrawUnits				units			= new DrawUnits();
	public DrawBuildingSmokes		buildingSmokes	= new DrawBuildingSmokes();
													
	public Draw						campaign		= new DrawCampaign();
	public DrawBlackScreen			blackScreen		= new DrawBlackScreen();
													
	public boolean					isDrawCursor	= false;
	public DrawCursor				cursorDefault	= new DrawCursor().setCursor(CursorImages().cursor);
													
	public LinkedHashSet<Draw>[]	draws			= new LinkedHashSet[DrawLevel.values().length];
													
	public float					maxOffsetY;
	public float					maxOffsetX;
	public float					minOffsetY;
	public float					minOffsetX;
	public boolean					isBlackScreen	= false;
													
	/*
	 * Порядок рисования:
	 * 
	 * клеточки
	 * войны не двигающиеся
	 * 
	 * ? войны двигающиеся
	 * ? искорки
	 * ? увеличение уровня
	 * ? искорки отравления/ауры
	 * ? поле хода/атаки война
	 * ? дым
	 * 
	 * инфо
	 */
	
	public void add(Draw draw)
	{
		add(draw, DrawLevel.MIDDLE);
	}
	
	public void add(Draw draw, DrawLevel level)
	{
		draws[level.ordinal()].add(draw);
	}
	
	public void remove(Draw draw)
	{
		remove(draw, DrawLevel.MIDDLE);
	}
	
	public void remove(Draw draw, DrawLevel level)
	{
		draws[level.ordinal()].remove(draw);
	}
	
	public DrawMain()
	{
		mapH = game.h * A;
		mapW = game.w * A;
		visibleMapH = h - info.h;
		visibleMapW = w;
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
		actionY = (visibleMapH - action.h) / 2;
		
		for (DrawLevel level : DrawLevel.values())
			draws[level.ordinal()] = new LinkedHashSet<>();
			
		add(campaign);
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
		
		if (isDrawCursor)
			cursorDefault.draw(canvas);
			
		for (DrawLevel level : DrawLevel.values())
			for (Iterator<Draw> iterator = draws[level.ordinal()].iterator(); iterator.hasNext();)
			{
				Draw draw = iterator.next();
				draw.draw(canvas);
				if (draw.isEnd())
					iterator.remove();
			}
			
		buildingSmokes.draw(canvas);
		canvas.restore();
		
		canvas.save();
		canvas.translate(0, h - info.h);
		infoNull.draw(canvas);
		infoMove.draw(canvas);
		canvas.translate(0, infoY);
		info.draw(canvas);
		canvas.restore();
		
		if (action.isActive())
		{
			canvas.save();
			canvas.translate(0, actionY);
			action.draw(canvas);
			canvas.restore();
		}
		
		blackScreen.draw(canvas);
		if (isBlackScreen)
			canvas.drawColor(Color.BLACK);
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
	
	public void touch(float touchY, float touchX)
	{
		// проверка по иксу немного странная ---
		// ведь GameView (пока что) всегда имеет ширину равный ширине экрана
		if (!isActiveGame || touchY < 0 || touchX < 0 || touchY > h - info.h || touchX > w)
			return;
		if (action.isActive())
		{
			action.touch(touchY - actionY, touchX);
			return;
		}
		
		int i = Math.round((touchY / mapScale - offsetY) / A - 0.5f);
		int j = Math.round((touchX / mapScale - offsetX) / A - 0.5f);
		if (0 <= i && i < game.h && 0 <= j && j < game.w)
			try
			{
				inputMain.tap(i, j);
			}
			catch (Exception e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
	}
	
	public void updateCursors()
	{
		cursorDefault.update();
	}
	
	public void focusOnCell(int i, int j)
	{
		float nextOffsetY = -i * A - A / 2 + visibleMapH / mapScale / 2;
		float nextOffsetX = -j * A - A / 2 + visibleMapW / mapScale / 2;
		setNextOffset(nextOffsetY, nextOffsetX);
	}
	
}
