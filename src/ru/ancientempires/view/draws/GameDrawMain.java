package ru.ancientempires.view.draws;

import java.util.ArrayList;
import java.util.Random;

import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.client.Client;
import ru.ancientempires.images.CursorImages;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.view.GameView;
import ru.ancientempires.view.algortihms.InputAlgorithmMain;
import ru.ancientempires.view.draws.campaign.GameDrawBlackScreen;
import ru.ancientempires.view.draws.campaign.GameDrawCameraMove;
import ru.ancientempires.view.draws.onframes.GameDrawBuildingSmokes;
import ru.ancientempires.view.draws.onframes.GameDrawUnitAttackMain;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMoveEnd;
import ru.ancientempires.view.draws.onframes.GameDrawUnitRaise;
import android.graphics.Canvas;
import android.graphics.Color;

public class GameDrawMain
{
	
	public GameActivity					gameActivity;
	public Game							game					= Client.getClient().getGame();
	
	public InputAlgorithmMain			inputAlgorithmMain;
	
	public Random						rnd						= new Random();
	
	public final int					gameDrawInfoH;
	public final int					gameDrawActionH;
	public final int					startActionY;
	
	volatile public int					nextOffsetY;
	volatile public int					nextOffsetX;
	public int							offsetY;
	public int							offsetX;
	
	public int							iFrame					= 0;
	
	public GameDrawCell					gameDrawCell;
	public GameDrawRange				gameDrawZoneMove		= new GameDrawRange(this).setCursor(CursorImages.cursorWay);
	public GameDrawRange				gameDrawZoneAttack		= new GameDrawRange(this).setCursor(CursorImages.cursorAttack);
	public GameDrawRange				gameDrawZoneRaise		= new GameDrawRange(this).setCursor(CursorImages.cursorAttack);
	public GameDrawCell					gameDrawCellDual;
	public GameDrawUnit					gameDrawUnit			= new GameDrawUnit(this);
	public GameDrawWayLine				gameDrawWayLine			= new GameDrawWayLine(this);
	
	public GameDrawBuildingSmokes		gameDrawBuildingSmokes;
	public GameDrawUnitMove				gameDrawUnitMove;
	public GameDrawUnitMoveEnd			gameDrawUnitMoveEnd		= new GameDrawUnitMoveEnd(this);
	public GameDrawUnitAttackMain		gameDrawUnitAttack		= new GameDrawUnitAttackMain(this);
	public GameDrawCellAttack			gameDrawCellAttack		= new GameDrawCellAttack(this);
	public GameDrawUnitRaise			gameDrawUnitRaise		= new GameDrawUnitRaise(this);
	public GameDrawUnitsHeal			gameDrawUnitsHeal		= new GameDrawUnitsHeal(this);
	
	public GameDrawAction				gameDrawAction;
	public GameDrawInfo					gameDrawInfo;
	public boolean						isActiveGame			= true;
	
	public GameDraw						gameDrawCampaign		= new GameDrawCampaign(this);
	public GameDrawBlackScreen			gameDrawBlackScreen		= new GameDrawBlackScreen(this);
	
	public boolean						isDrawCursor			= true;
	public GameDrawCursor				gameDrawCursorDefault	= new GameDrawCursor(this).setCursor(CursorImages.cursor);
	public GameDrawCursor				gameDrawCursorMove		= new GameDrawCursor(this).setCursor(CursorImages.cursorPointerWay);
	public GameDrawCursor				gameDrawCursorAttack	= new GameDrawCursor(this).setCursor(CursorImages.cursorPointerAttack);
	public ArrayList<GameDrawCursor>	gameDrawCursors;
	
	public ArrayList<GameDraw>			gameDraws				= new ArrayList<GameDraw>();
	public ArrayList<GameDraw>			gameDrawsEffects		= new ArrayList<GameDraw>();
	
	public int							maxOffsetY;
	public int							maxOffsetX;
	public int							minOffsetY;
	public int							minOffsetX;
	
	public GameDrawMain()
	{
		this.gameDrawInfoH = GameDraw.A + 8 * 2;// this.gameDrawInfo.a;
		this.gameDrawActionH = GameDraw.A * 4 / 3;
		this.startActionY = GameView.h - this.gameDrawActionH;
		
		this.maxOffsetY = this.nextOffsetY = this.gameDrawInfoH;
		this.maxOffsetX = this.nextOffsetX = 0;
		this.minOffsetY = -(this.game.map.h * GameDraw.A - GameView.h + this.gameDrawActionH);
		this.minOffsetX = -(this.game.map.w * GameDraw.A - GameView.w);
		
		this.gameDrawAction = new GameDrawAction(this);
		this.gameDrawInfo = new GameDrawInfo(this);
		
		this.gameDrawCell = new GameDrawCell(this);
		this.gameDrawCellDual = new GameDrawCell(this).setDual();
		
		GameDrawUnitMove.framesForCell = 8;
		GameDrawCameraMove.delta = GameDraw.a * 12;
		this.gameDrawUnitMove = new GameDrawUnitMove(this);
		
		this.gameDraws.add(this.gameDrawCell);
		this.gameDraws.add(this.gameDrawZoneMove);
		this.gameDraws.add(this.gameDrawZoneAttack);
		this.gameDraws.add(this.gameDrawZoneRaise);
		this.gameDraws.add(this.gameDrawCellDual);
		this.gameDraws.add(this.gameDrawUnit);
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
		
		canvas.translate(this.offsetX, this.offsetY);
		for (GameDraw gameDraw : this.gameDraws)
			gameDraw.draw(canvas);
		
		if (this.isDrawCursor)
			for (GameDrawCursor gameDrawCursor : this.gameDrawCursors)
				gameDrawCursor.draw(canvas);
		
		for (GameDraw gameDraw : this.gameDrawsEffects)
			gameDraw.draw(canvas);
		canvas.translate(-this.offsetX, -this.offsetY);
		
		this.gameDrawInfo.draw(canvas);
		if (this.isActiveGame)
		{
			canvas.translate(0, this.startActionY);
			this.gameDrawAction.draw(canvas);
			canvas.translate(0, -this.startActionY);
		}
		this.gameDrawBlackScreen.draw(canvas);
		
		FewBitmaps.ordinal = this.iFrame / 8;
	}
	
	public void updateOffset()
	{
		this.nextOffsetY = Math.max(this.minOffsetY, Math.min(this.maxOffsetY, this.nextOffsetY));
		this.nextOffsetX = Math.max(this.minOffsetX, Math.min(this.maxOffsetX, this.nextOffsetX));
	}
	
	public boolean touch(float touchY, float touchX)
	{
		if (!this.isActiveGame)
			return false;
		if (touchY < this.gameDrawInfoH)
			return true;
		else if (touchY < this.startActionY)
			return false;
		else
		{
			this.gameDrawAction.touch(touchY, touchX);
			return true;
		}
	}
	
	public void updateCursors(Game game)
	{
		for (GameDrawCursor gameDrawCursor : this.gameDrawCursors)
			gameDrawCursor.update(game);
	}
	
	public void focusOnCell(int i, int j)
	{
		int availableY = this.startActionY - this.gameDrawInfoH;
		int availableX = GameView.w;
		
		int newOffsetY = -i * GameDraw.A - GameDraw.A / 2 + availableY / 2;
		int newOffsetX = -j * GameDraw.A - GameDraw.A / 2 + availableX / 2;
		
		this.nextOffsetY = newOffsetY + this.gameDrawInfoH;
		this.nextOffsetX = newOffsetX;
		updateOffset();
	}
	
	public void startGame()
	{
		Campaign.start();
	}
	
}
