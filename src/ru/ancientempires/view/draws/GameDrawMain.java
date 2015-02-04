package ru.ancientempires.view.draws;

import java.util.ArrayList;

import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.images.CursorImages;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.view.GameView;
import ru.ancientempires.view.algortihms.InputAlgoritmMain;
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
	
	public InputAlgoritmMain			inputAlgoritmMain;
	
	public final int					gameDrawInfoH;
	public final int					gameDrawActionH;
	private final int					startActionY;
	
	volatile public int					offsetY;
	volatile public int					offsetX;
	public int							currentOffsetY;
	public int							currentOffsetX;
	
	public int							iFrame					= 0;
	
	public GameDrawCell					gameDrawCell;
	public GameDrawRange				gameDrawZoneMove		= new GameDrawRange(this).setCursor(CursorImages.cursorWay);
	public GameDrawRange				gameDrawZoneAttack		= new GameDrawRange(this).setCursor(CursorImages.cursorAttack);
	public GameDrawRange				gameDrawZoneRaise		= new GameDrawRange(this).setCursor(CursorImages.cursorAttack);
	public GameDrawCell					gameDrawCellDual;
	public GameDrawUnit					gameDrawUnit			= new GameDrawUnit(this);
	public GameDrawWayLine				gameDrawWayLine			= new GameDrawWayLine(this);
	
	public GameDrawUnitMove				gameDrawUnitMove		= new GameDrawUnitMove(this);
	public GameDrawUnitMoveEnd			gameDrawUnitMoveEnd		= new GameDrawUnitMoveEnd(this);
	public GameDrawUnitAttackMain		gameDrawUnitAttack		= new GameDrawUnitAttackMain(this);
	public GameDrawCellAttack			gameDrawCellAttack		= new GameDrawCellAttack(this);
	public GameDrawUnitRaise			gameDrawUnitRaise		= new GameDrawUnitRaise(this);
	
	public GameDrawAction				gameDrawAction;
	public GameDrawInfo					gameDrawInfo;
	
	public GameDrawCursor				gameDrawCursorDefault	= new GameDrawCursor(this).setCursor(CursorImages.cursor);
	public GameDrawCursor				gameDrawCursorMove		= new GameDrawCursor(this).setCursor(CursorImages.cursorPointerWay);
	public GameDrawCursor				gameDrawCursorAttack	= new GameDrawCursor(this).setCursor(CursorImages.cursorPointerAttack);
	public ArrayList<GameDrawCursor>	gameDrawCursors;
	
	public ArrayList<GameDraw>			gameDraws				= new ArrayList<GameDraw>();
	public ArrayList<GameDraw>			gameDrawsEffects		= new ArrayList<GameDraw>();
	
	public int							minOffsetY;
	public int							minOffsetX;
	public int							maxOffsetY;
	public int							maxOffsetX;
	
	public GameDrawMain()
	{
		this.gameDrawInfoH = GameDraw.A + 8 * 2;// this.gameDrawInfo.a;
		this.gameDrawActionH = GameDraw.A * 4 / 3;
		this.startActionY = GameView.h - this.gameDrawActionH;
		
		this.minOffsetY = this.offsetY = this.gameDrawInfoH;
		this.minOffsetX = this.offsetX = 0;
		this.maxOffsetY = -(this.game.map.h * GameDraw.A - GameView.h + this.gameDrawActionH);
		this.maxOffsetX = -(this.game.map.w * GameDraw.A - GameView.w);
		
		this.gameDrawAction = new GameDrawAction(this);
		this.gameDrawInfo = new GameDrawInfo(this);
		
		this.gameDrawCell = new GameDrawCell(this);
		this.gameDrawCellDual = new GameDrawCell(this).setDual();
		
		this.gameDraws.add(this.gameDrawCell);
		this.gameDraws.add(this.gameDrawZoneMove);
		this.gameDraws.add(this.gameDrawZoneAttack);
		this.gameDraws.add(this.gameDrawZoneRaise);
		this.gameDraws.add(this.gameDrawCellDual);
		this.gameDraws.add(this.gameDrawUnit);
		this.gameDraws.add(this.gameDrawWayLine);
		this.gameDraws.add(this.gameDrawUnitMove);
		
		this.gameDrawsEffects.add(this.gameDrawUnitMoveEnd);
		this.gameDrawsEffects.add(this.gameDrawUnitAttack);
		this.gameDrawsEffects.add(this.gameDrawCellAttack);
		this.gameDrawsEffects.add(this.gameDrawUnitRaise);
		
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
		this.currentOffsetY = this.offsetY;
		this.currentOffsetX = this.offsetX;
		canvas.drawColor(Color.WHITE);
		
		canvas.translate(this.currentOffsetX, this.currentOffsetY);
		for (GameDraw gameDraw : this.gameDraws)
			gameDraw.draw(canvas);
		
		for (GameDrawCursor gameDrawCursor : this.gameDrawCursors)
			gameDrawCursor.draw(canvas);
		
		for (GameDraw gameDraw : this.gameDrawsEffects)
			gameDraw.draw(canvas);
		canvas.translate(-this.currentOffsetX, -this.currentOffsetY);
		
		this.gameDrawInfo.draw(canvas);
		
		canvas.translate(0, this.startActionY);
		this.gameDrawAction.draw(canvas);
		canvas.translate(0, -this.startActionY);
		
		FewBitmaps.ordinal = this.iFrame / 8;
	}
	
	public void updateOffset()
	{
		if (this.offsetY > this.minOffsetY)
			this.offsetY = this.minOffsetY;
		else if (this.offsetY < this.maxOffsetY)
			this.offsetY = this.maxOffsetY;
		if (this.offsetX > this.minOffsetX)
			this.offsetX = this.minOffsetX;
		else if (this.offsetX < this.maxOffsetX)
			this.offsetX = this.maxOffsetX;
	}
	
	public boolean touch(float touchY, float touchX)
	{
		if (touchY < this.startActionY)
			return true;
		else
		{
			this.gameDrawAction.touch(touchY, touchX);
			return false;
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
		
		this.offsetY = newOffsetY + this.gameDrawInfoH;
		this.offsetX = newOffsetX;
		updateOffset();
	}
	
}
