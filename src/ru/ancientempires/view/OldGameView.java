package ru.ancientempires.view;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.SomeWithBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.UnitType;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

public class OldGameView extends FrameLayout
{
	
	public static boolean	isBaseCellSizeDetermine	= false;
	
	public static int		baseH;
	public static int		baseW;
	public static int		A;
	public static int		a;										// 1/12 A
																	
	public static float		baseMulti				= 4.5f / 3.0f;
	
	// не знаю, как выводится эта константа
	public static final int	DELAY_BETWEEN_UPDATES	= 265;
	
	public static void initResources(Resources res) throws IOException
	{
		GameViewCursor.init();
		GameViewUnit.init();
		GameViewAction.initResources();
	}
	
	public GameActivity			gameActivity;
	
	// Модель игры
	private final Client		client			= Client.getClient();
	private final Game			game			= this.client.getGame();
	
	// Отображение игры
	private GameViewPart		gameViewCell;
	private ZoneView			zoneViewWay;
	private ZoneView			zoneViewAttack;
	private GameViewPart		gameViewCellDual;
	private GameViewUnit		gameViewUnit;
	private GameViewPart		gameViewRaise;
	private GameViewPart		gameViewAction;
	private GameViewInfo		gameViewInfo;
	public GameViewCursor		gameViewCursor;
	private AnimateAttackView	animateAttackView;
	
	public static LayoutParams	fullLayoutParams;
	
	private GestureDetector		gestureDetector;
	public Timer				timer;
	private Handler				updateHandler;
	
	public int					offsetY			= 0;
	public int					offsetX			= 0;
	
	public int					lastTapI;
	public int					lastTapJ;
	
	public boolean				isWayVisible	= false;
	public boolean				isAttackVisible	= false;
	
	public OldGameView(Context context)
	{
		super(context);
		
		setWillNotDraw(false);
		setClipChildren(false);
		OldGameView.fullLayoutParams = new LayoutParams(
				this.game.map.w * OldGameView.A, this.game.map.h * OldGameView.A);
		
		this.gameViewCell = new GameViewCell(getContext(), this)
				.setDual(false).setField(this.game.map.getField());
		this.zoneViewWay = new ZoneView(getContext(), this);
		this.zoneViewAttack = new ZoneView(getContext(), this);
		this.gameViewCellDual = new GameViewCell(getContext(), this)
				.setDual(true).setField(this.game.map.getField());
		
		this.gameViewUnit = new GameViewUnit(getContext(), this)
				.setField(this.game.fieldUnits)
				.setWayView(this.zoneViewWay)
				.setAttackView(this.zoneViewAttack);
		this.gameViewCursor = new GameViewCursor(getContext(), this);
		this.gameViewAction = new GameViewAction(getContext(), this);
		this.gameViewInfo = new GameViewInfo(getContext(), this).setField(this.game.map.getField())
				.updatePlayer(this.game.currentPlayer);
		
		this.animateAttackView = new AnimateAttackView(getContext());
		this.gameViewUnit
				.setGameViewCursor(this.gameViewCursor)
				.setAnimateAttackView(this.animateAttackView);
		
		addView(this.gameViewCell);
		addView(this.zoneViewWay);
		addView(this.zoneViewAttack);
		addView(this.gameViewCellDual);
		addView(this.gameViewUnit);
		addView(this.gameViewCursor);
		addView(this.animateAttackView);
		addView(this.gameViewAction);
		addView(this.gameViewInfo);
		
		this.gameViewCell.setLayoutParams(OldGameView.fullLayoutParams);
		this.gameViewUnit.setLayoutParams(OldGameView.fullLayoutParams);
		this.gameViewCellDual.setLayoutParams(OldGameView.fullLayoutParams);
		
		this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDown(MotionEvent e)
			{
				return true;
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent event)
			{
				final float y = event.getY() - OldGameView.this.offsetY;
				final float x = event.getX() - OldGameView.this.offsetX;
				
				final int i = (int) y / OldGameView.baseH - (y < 0 ? 1 : 0);
				final int j = (int) x / OldGameView.baseW - (x < 0 ? 1 : 0);
				
				return tap(i, j);
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
			{
				OldGameView.this.offsetY -= distanceY;
				OldGameView.this.offsetX -= distanceX;
				updateOffset();
				return true;
			}
		});
		
		this.updateHandler = new Handler(new Handler.Callback()
		{
			@Override
			public boolean handleMessage(Message msg)
			{
				updateCells();
				return true;
			}
		});
		
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				OldGameView.this.updateHandler.sendMessage(new Message());
			}
		}, 0, OldGameView.DELAY_BETWEEN_UPDATES);
		
		tap(this.game.currentPlayer.cursorI, this.game.currentPlayer.cursorJ);
	}
	
	private void updateOffset()
	{
		this.gameViewCell.setY(this.offsetY);
		this.gameViewCell.setX(this.offsetX);
		this.gameViewUnit.setY(this.offsetY);
		this.gameViewUnit.setX(this.offsetX);
		this.gameViewCellDual.setY(this.offsetY);
		this.gameViewCellDual.setX(this.offsetX);
		this.gameViewCursor.setY(this.offsetY);
		this.gameViewCursor.setX(this.offsetX);
		this.animateAttackView.setY(this.offsetY);
		this.animateAttackView.setX(this.offsetX);
		this.gameViewUnit.setZoneViewOffset();
	}
	
	private void updateCells()
	{
		SomeWithBitmaps.ordinal++;
		this.gameViewUnit.invalidate();
		this.gameViewCursor.invalidate();
	}
	
	private boolean tap(int i, int j)
	{
		if (i == this.lastTapI && j == this.lastTapJ)
			return false;
		
		this.lastTapI = i;
		this.lastTapJ = j;
		this.game.currentPlayer.cursorI = i;
		this.game.currentPlayer.cursorJ = j;
		
		if (!this.game.map.validateCoord(i, j))
			return false;
		
		return this.gameViewInfo.update()
			|| this.gameViewCursor.update()
			|| this.gameViewUnit.update()
			|| this.gameViewAction.update();
	}
	
	public void performAction(ActionType actionType)
	{
		boolean isAction;
		
		this.isWayVisible = false;
		this.isAttackVisible = false;
		
		if (actionType == ActionType.ACTION_UNIT_MOVE)
		{
			this.isWayVisible = true;
			isAction = this.gameViewUnit.performAction(actionType);
		}
		else if (actionType == ActionType.ACTION_UNIT_REPAIR || actionType == ActionType.ACTION_UNIT_CAPTURE)
		{
			Action action = new Action(actionType);
			action.setProperty("i", this.lastTapI);
			action.setProperty("j", this.lastTapJ);
			Client.action(action);
			
			isAction = this.gameViewCell.update() || this.gameViewUnit.performAction(actionType);
		}
		else if (actionType == ActionType.ACTION_UNIT_ATTACK)
		{
			this.isAttackVisible = true;
			isAction = this.gameViewUnit.performAction(actionType);
			this.gameViewInfo.updatePlayer(this.game.currentPlayer);
		}
		else if (actionType == ActionType.ACTION_UNIT_RAISE)
		{
			isAction = this.gameViewRaise.performAction(actionType);
			this.gameViewInfo.updatePlayer(this.game.currentPlayer);
		}
		else if (actionType == ActionType.ACTION_CELL_BUY)
		{
			Action action = new Action(ActionType.GET_CELL_BUY_UNITS);
			action.setProperty("i", this.lastTapI);
			action.setProperty("j", this.lastTapJ);
			ActionResult result = Client.action(action);
			UnitType[] units = (UnitType[]) result.getProperty("units");
			this.gameActivity.startUnitBuyActivity(units);
			isAction = false;
		}
		else if (actionType == ActionType.ACTION_END_TURN)
		{
			Action action = new Action(actionType);
			Client.action(action);
			
			isAction = this.gameViewUnit.performAction(actionType);
			this.gameViewInfo.updatePlayer(this.game.currentPlayer);
			Toast.makeText(getContext(), "Новый Ход!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			MyAssert.a(false);
			isAction = false;
		}
		if (isAction)
			this.gameViewAction.update();
	}
	
	public void performActionBuy(UnitType type)
	{
		Action action = new Action(ActionType.ACTION_CELL_BUY);
		action.setProperty("i", this.lastTapI);
		action.setProperty("j", this.lastTapJ);
		action.setProperty("type", type.ordinal);
		Client.action(action);
		
		this.gameViewInfo.updatePlayer(this.game.currentPlayer);
		this.gameViewCell.update();
		this.gameViewAction.update();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		Log.d("AE", "" + e.getAction());
		if (this.gestureDetector.onTouchEvent(e))
			return true;
		else
			return performClick();
	}
	
	// TODO разобраться что это такое
	@Override
	public boolean performClick()
	{
		Log.d("AE", "performClick");
		return super.performClick();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		int rectH = (int) (OldGameView.baseH * 4 / 3.0);
		this.gameViewAction.setY(h - rectH);
		this.gameViewAction.setX(0);
		LayoutParams layoutParamsAction = new LayoutParams(w, rectH);
		this.gameViewAction.setLayoutParams(layoutParamsAction);
		
		this.gameViewInfo.setY(0);
		this.gameViewInfo.setX(0);
		LayoutParams layoutParamsInfo = new LayoutParams(w, rectH);
		this.gameViewInfo.setLayoutParams(layoutParamsInfo);
	}
	
}
