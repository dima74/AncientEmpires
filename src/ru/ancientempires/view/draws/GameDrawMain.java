package ru.ancientempires.view.draws;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.CursorImages;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.view.GameView;
import ru.ancientempires.view.algortihms.InputAlgorithmMain;
import ru.ancientempires.view.draws.campaign.GameDrawCameraMove;
import ru.ancientempires.view.draws.onframes.GameDrawBlackScreen;
import ru.ancientempires.view.draws.onframes.GameDrawBuildingSmokes;
import ru.ancientempires.view.draws.onframes.GameDrawUnitAttackMain;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMoveEnd;
import ru.ancientempires.view.draws.onframes.GameDrawUnitRaise;

public class GameDrawMain
{
	
	public GameActivity	gameActivity;
	public Game			game	= Client.getClient().getGame();
	
	public InputAlgorithmMain inputAlgorithmMain;
	
	public Random rnd = new Random();
	
	public int	gameDrawInfoH;
	public int	gameDrawActionH;
	public int	mapH;
	public int	mapW;
	public int	visibleMapH;
	public int	visibleMapW;
	
	volatile public float	nextOffsetY;
	volatile public float	nextOffsetX;
	public float			offsetY;
	public float			offsetX;
	
	public int iFrame = 0;
	
	public GameDrawCells		gameDrawCells;
	public GameDrawRange		gameDrawZoneMove	= new GameDrawRange(this).setCursor(CursorImages.cursorWay);
	public GameDrawRange		gameDrawZoneAttack	= new GameDrawRange(this).setCursor(CursorImages.cursorAttack);
	public GameDrawRange		gameDrawZoneRaise	= new GameDrawRange(this).setCursor(CursorImages.cursorAttack);
	public GameDrawCells		gameDrawCellDual;
	public GameDrawUnits		gameDrawUnits		= new GameDrawUnits(this);
	public GameDrawUnitsDead	gameDrawUnitsDead	= new GameDrawUnitsDead(this);
	public GameDrawWayLine		gameDrawWayLine		= new GameDrawWayLine(this);
	
	public GameDrawBuildingSmokes	gameDrawBuildingSmokes;
	public GameDrawUnitMove			gameDrawUnitMove;
	public GameDrawUnitMoveEnd		gameDrawUnitMoveEnd	= new GameDrawUnitMoveEnd(this);
	public GameDrawUnitAttackMain	gameDrawUnitAttack	= new GameDrawUnitAttackMain(this);
	public GameDrawCellAttack		gameDrawCellAttack	= new GameDrawCellAttack(this);
	public GameDrawUnitRaise		gameDrawUnitRaise	= new GameDrawUnitRaise(this);
	public GameDrawUnitsHeal		gameDrawUnitsHeal	= new GameDrawUnitsHeal(this);
	
	public GameDrawAction	gameDrawAction;
	public GameDrawInfo		gameDrawInfo;
	public boolean			isActiveGame	= true;
	
	public GameDraw				gameDrawCampaign	= new GameDrawCampaign(this);
	public GameDrawBlackScreen	gameDrawBlackScreen	= new GameDrawBlackScreen(this);
	
	public boolean						isDrawCursor			= true;
	public GameDrawCursor				gameDrawCursorDefault	= new GameDrawCursor(this).setCursor(CursorImages.cursor);
	public GameDrawCursor				gameDrawCursorMove		= new GameDrawCursor(this).setCursor(CursorImages.cursorPointerWay);
	public GameDrawCursor				gameDrawCursorAttack	= new GameDrawCursor(this).setCursor(CursorImages.cursorPointerAttack);
	public ArrayList<GameDrawCursor>	gameDrawCursors;
	
	public ArrayList<GameDraw>	gameDraws			= new ArrayList<GameDraw>();
	public ArrayList<GameDraw>	gameDrawsEffects	= new ArrayList<GameDraw>();
	
	public float	maxOffsetY;
	public float	maxOffsetX;
	public float	minOffsetY;
	public float	minOffsetX;
	public boolean	isBlackScreen	= false;
	
	public GameDrawMain()
	{
		this.gameDrawInfoH = GameDrawInfo.mA + 8 * 2;// this.gameDrawInfo.a;
		this.gameDrawActionH = GameDrawAction.mA - 8;
		this.mapH = this.game.h * GameDraw.A;
		this.mapW = this.game.w * GameDraw.A;
		
		this.gameDrawAction = new GameDrawAction(this);
		this.gameDrawInfo = new GameDrawInfo(this);
		
		updateOffsetBounds();
		
		this.nextOffsetY = -(this.mapH - this.visibleMapH / GameDraw.mapScale) / 2;
		this.nextOffsetX = -(this.mapW - this.visibleMapW / GameDraw.mapScale) / 2;
		
		this.gameDrawCells = new GameDrawCells(this);
		this.gameDrawCellDual = new GameDrawCells(this).setDual();
		
		GameDrawUnitMove.framesForCell = 8;
		GameDrawCameraMove.delta = GameDraw.a * 12;
		this.gameDrawUnitMove = new GameDrawUnitMove(this);
		
		this.gameDraws.add(this.gameDrawCells);
		this.gameDraws.add(this.gameDrawZoneMove);
		this.gameDraws.add(this.gameDrawZoneAttack);
		this.gameDraws.add(this.gameDrawZoneRaise);
		this.gameDraws.add(this.gameDrawCellDual);
		this.gameDraws.add(this.gameDrawUnitsDead);
		this.gameDraws.add(this.gameDrawUnits);
		this.gameDraws.add(this.gameDrawWayLine);
		this.gameDraws.add(this.gameDrawUnitMove);
		
		this.gameDrawBuildingSmokes = new GameDrawBuildingSmokes(this);
		
		this.gameDrawsEffects.add(this.gameDrawBuildingSmokes);
		this.gameDrawsEffects.add(this.gameDrawUnitMoveEnd);
		this.gameDrawsEffects.add(this.gameDrawUnitAttack);
		this.gameDrawsEffects.add(this.gameDrawCellAttack);
		this.gameDrawsEffects.add(this.gameDrawUnitRaise);
		this.gameDrawsEffects.add(this.gameDrawUnitsHeal);
		this.gameDrawsEffects.add(this.gameDrawCampaign);
		
		this.gameDrawCursors = new ArrayList<GameDrawCursor>();
		this.gameDrawCursors.add(this.gameDrawCursorDefault);
	}
	
	private void updateOffsetBounds()
	{
		this.visibleMapH = GameView.h - this.gameDrawInfoH;
		this.visibleMapW = GameView.w;
		if (this.gameDrawAction.isActive)
			this.visibleMapH -= this.gameDrawActionH;
		this.minOffsetY = this.maxOffsetY = -(this.mapH - this.visibleMapH / GameDraw.mapScale) / 2;
		this.minOffsetX = this.maxOffsetX = -(this.mapW - this.visibleMapW / GameDraw.mapScale) / 2;
		if (this.minOffsetY < 0)
		{
			this.minOffsetY = -(this.mapH - this.visibleMapH / GameDraw.mapScale);
			this.maxOffsetY = 0;
		}
		if (this.minOffsetX < 0)
		{
			this.minOffsetX = -(this.mapW - this.visibleMapW / GameDraw.mapScale);
			this.maxOffsetX = 0;
		}
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		for (GameDraw gameDraw : this.gameDraws)
			gameDraw.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void draw(Canvas canvas)
	{
		updateOffset();
		this.offsetY = this.nextOffsetY;
		this.offsetX = this.nextOffsetX;
		canvas.drawColor(Color.WHITE);
		
		canvas.save();
		canvas.scale(GameDraw.mapScale, GameDraw.mapScale);
		canvas.translate(this.offsetX, this.offsetY);
		for (GameDraw gameDraw : this.gameDraws)
			gameDraw.draw(canvas);
			
		if (this.isDrawCursor)
			for (GameDrawCursor gameDrawCursor : this.gameDrawCursors)
				gameDrawCursor.draw(canvas);
				
		for (GameDraw gameDraw : this.gameDrawsEffects)
			gameDraw.draw(canvas);
		// canvas.translate(-this.offsetX, -this.offsetY);
		canvas.restore();
		
		canvas.translate(0, GameView.h - this.gameDrawInfoH);
		this.gameDrawInfo.draw(canvas);
		if (this.isActiveGame)
		{
			canvas.translate(0, -this.gameDrawActionH);
			this.gameDrawAction.draw(canvas);
			canvas.translate(0, this.gameDrawActionH);
		}
		canvas.translate(0, -(GameView.h - this.gameDrawInfoH));
		this.gameDrawBlackScreen.draw(canvas);
		if (this.isBlackScreen)
			canvas.drawColor(Color.WHITE);
			
		FewBitmaps.ordinal = this.iFrame / 8;
	}
	
	public void updateOffset()
	{
		this.nextOffsetY = Math.max(this.minOffsetY, Math.min(this.maxOffsetY, this.nextOffsetY));
		this.nextOffsetX = Math.max(this.minOffsetX, Math.min(this.maxOffsetX, this.nextOffsetX));
	}
	
	public void touch(float touchY, float touchX)
	{
		if (!this.isActiveGame || touchY > GameView.h - this.gameDrawInfoH)
			return;
		if (this.gameDrawAction.isActive && touchY > this.visibleMapH)
			this.gameDrawAction.touch(touchY, touchX);
		else
		{
			int i = (int) ((touchY / GameDraw.mapScale - this.offsetY) / GameDraw.A);
			int j = (int) ((touchX / GameDraw.mapScale - this.offsetX) / GameDraw.A);
			try
			{
				this.inputAlgorithmMain.tap(i, j);
			}
			catch (Exception e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
		}
	}
	
	public void updateCursors(Game game)
	{
		for (GameDrawCursor gameDrawCursor : this.gameDrawCursors)
			gameDrawCursor.update(game);
	}
	
	public void focusOnCell(int i, int j)
	{
		this.nextOffsetY = -i * GameDraw.A - GameDraw.A / 2 + this.visibleMapH / GameDraw.mapScale / 2;
		this.nextOffsetX = -j * GameDraw.A - GameDraw.A / 2 + this.visibleMapW / GameDraw.mapScale / 2;
		updateOffset();
	}
	
	public void startGame()
	{
		Campaign.start();
	}
	
}
