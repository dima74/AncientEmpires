package ru.ancientempires.view.draws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawBlackScreen;
import ru.ancientempires.view.draws.onframes.GameDrawBuildingSmokes;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMoveEnd;
import ru.ancientempires.view.draws.onframes.GameDrawUnitRaise;
import ru.ancientempires.view.inputs.InputMain;
import ru.ancientempires.view.inputs.InputPlayer;

public class GameDrawMain extends GameDraw
{
	
	public static GameDrawMain main;
	
	public InputMain	inputMain;
	public InputPlayer	inputPlayer;
	
	public Random rnd = new Random();
	
	public int	mapH;
	public int	mapW;
	public int	visibleMapH;
	public int	visibleMapW;
	public int	iFrame;
	
	volatile public float	nextOffsetY;
	volatile public float	nextOffsetX;
	public float			offsetY;
	public float			offsetX;
	
	private int				gameDrawActionY;
	public GameDrawAction	gameDrawAction		= new GameDrawAction();
	public GameDrawInfoNull	gameDrawInfoNull	= new GameDrawInfoNull();
	public GameDrawInfo		gameDrawInfo		= new GameDrawInfo();
	public GameDrawInfoMove	gameDrawInfoMove	= new GameDrawInfoMove();
	public int				gameDrawInfoY		= gameDrawInfo.h;
	volatile public boolean	isActiveGame		= true;
	
	{
		GameDrawMain.main = this;
	}
	
	public GameDrawCells			gameDrawCells			= new GameDrawCells();
	// public GameDrawRange gameDrawZoneMove = new GameDrawRange().setCursor(CursorImages.cursorWay);
	// public GameDrawRange gameDrawZoneAttack = new GameDrawRange().setCursor(CursorImages.cursorAttack);
	// public GameDrawRange gameDrawZoneRaise = new GameDrawRange().setCursor(CursorImages.cursorAttack);
	public GameDrawCells			gameDrawCellDual		= new GameDrawCells().setDual();
	public GameDrawUnitsDead		gameDrawUnitsDead		= new GameDrawUnitsDead();
	public GameDrawUnits			gameDrawUnits			= new GameDrawUnits();
	// public GameDrawWayLine gameDrawWayLine = new GameDrawWayLine();
	public GameDrawBuildingSmokes	gameDrawBuildingSmokes	= new GameDrawBuildingSmokes();
	
	public GameDrawPlayer		gameDrawPlayer		= new GameDrawPlayer();
	public GameDrawRangeAll		gameDrawRangeAll	= new GameDrawRangeAll();
	// public GameDrawUnitMove gameDrawUnitMove = new GameDrawUnitMove();
	public GameDrawUnitMoveEnd	gameDrawUnitMoveEnd	= new GameDrawUnitMoveEnd();
	// public GameDrawUnitAttackMain gameDrawUnitAttack = new GameDrawUnitAttackMain();
	public GameDrawCellAttack	gameDrawCellAttack	= new GameDrawCellAttack();
	public GameDrawUnitRaise	gameDrawUnitRaise	= new GameDrawUnitRaise();
	public GameDrawUnitsHeal	gameDrawUnitsHeal	= new GameDrawUnitsHeal();
	
	public GameDraw				gameDrawCampaign	= new GameDrawCampaign();
	public GameDrawBlackScreen	gameDrawBlackScreen	= new GameDrawBlackScreen();
	
	public boolean						isDrawCursor			= false;
	public GameDrawCursor				gameDrawCursorDefault	= new GameDrawCursor().setCursor(CursorImages().cursor);
	public GameDrawCursor				gameDrawCursorMove		= new GameDrawCursor().setCursor(CursorImages().cursorPointerWay);
	public GameDrawCursor				gameDrawCursorAttack	= new GameDrawCursor().setCursor(CursorImages().cursorPointerAttack);
	public ArrayList<GameDrawCursor>	gameDrawCursors			= new ArrayList<GameDrawCursor>(Arrays.asList(gameDrawCursorDefault));
	
	public ArrayList<GameDraw>	gameDraws			= new ArrayList<GameDraw>();
	public ArrayList<GameDraw>	gameDrawsEffects	= new ArrayList<GameDraw>();
	
	public float	maxOffsetY;
	public float	maxOffsetX;
	public float	minOffsetY;
	public float	minOffsetX;
	public boolean	isBlackScreen	= false;
	
	public GameDrawMain()
	{
		mapH = game.h * A;
		mapW = game.w * A;
		visibleMapH = h - gameDrawInfo.h;
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
		gameDrawActionY = (visibleMapH - gameDrawAction.h) / 2;
		
		gameDraws.add(gameDrawCells);
		// this.gameDraws.add(this.gameDrawZoneMove);
		// this.gameDraws.add(this.gameDrawZoneAttack);
		// this.gameDraws.add(this.gameDrawZoneRaise);
		gameDraws.add(gameDrawCellDual);
		gameDraws.add(gameDrawUnitsDead);
		gameDraws.add(gameDrawUnits);
		// gameDraws.add(gameDrawWayLine);
		gameDraws.add(gameDrawBuildingSmokes);
		gameDraws.add(gameDrawCampaign);
		
		gameDrawsEffects.add(gameDrawPlayer);
		gameDrawsEffects.add(gameDrawRangeAll);
		// gameDrawsEffects.add(gameDrawUnitMove);
		gameDrawsEffects.add(gameDrawUnitMoveEnd);
		// gameDrawsEffects.add(gameDrawUnitAttack);
		gameDrawsEffects.add(gameDrawCellAttack);
		gameDrawsEffects.add(gameDrawUnitRaise);
		gameDrawsEffects.add(gameDrawUnitsHeal);
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
		for (GameDraw gameDraw : gameDraws)
			draw(canvas);
		if (isDrawCursor)
			for (GameDrawCursor gameDrawCursor : gameDrawCursors)
				gameDrawCursor.draw(canvas);
		for (GameDraw gameDraw : gameDrawsEffects)
			draw(canvas);
		canvas.restore();
		
		canvas.save();
		canvas.translate(0, h - gameDrawInfo.h);
		gameDrawInfoNull.draw(canvas);
		gameDrawInfoMove.draw(canvas);
		canvas.translate(0, gameDrawInfoY);
		gameDrawInfo.draw(canvas);
		canvas.restore();
		
		if (gameDrawAction.isActive())
		{
			canvas.save();
			canvas.translate(0, gameDrawActionY);
			gameDrawAction.draw(canvas);
			canvas.restore();
		}
		
		gameDrawBlackScreen.draw(canvas);
		if (isBlackScreen)
			canvas.drawColor(Color.BLACK);
	}
	
	synchronized public void onScroll(float distanceY, float distanceX)
	{
		setNextOffset(nextOffsetY - distanceY, nextOffsetX - distanceX);
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
		touchY /= mapScale;
		touchX /= mapScale;
		if (!isActiveGame || touchY > h - gameDrawInfo.h)
			return;
		if (gameDrawAction.isActive())
		{
			gameDrawAction.destroy();
			if (gameDrawAction.touch(touchY - gameDrawActionY, touchX))
				return;
		}
		
		int i = (int) ((touchY / mapScale - offsetY) / A);
		int j = (int) ((touchX / mapScale - offsetX) / A);
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
		for (GameDrawCursor gameDrawCursor : gameDrawCursors)
			gameDrawCursor.update();
	}
	
	public void focusOnCell(int i, int j)
	{
		float nextOffsetY = -i * A - A / 2 + visibleMapH / mapScale / 2;
		float nextOffsetX = -j * A - A / 2 + visibleMapW / mapScale / 2;
		setNextOffset(nextOffsetY, nextOffsetX);
	}
	
}
