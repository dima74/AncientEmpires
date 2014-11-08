package ru.ancientempires.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ru.ancientempires.SomeWithBitmaps;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Map;
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

public class GameView extends FrameLayout
{
	
	public static boolean		isBaseCellSizeDetermine	= false;
	public static int			baseH;
	public static int			baseW;
	public static float			baseHMulti				= 6 / 3.0f;
	public static float			baseWMulti				= GameView.baseHMulti;
	
	// не знаю, как выводится эта константа
	private static final int	DELAY_BETWEEN_UPDATES	= 265;
	
	public static void initResources(Resources res) throws IOException
	{
		GameViewCursor.init();
		GameViewUnit.init();
		GameViewAction.initResources();
	}
	
	public GameActivity				gameActivity;
	
	// Модель игры
	private Client					client			= Client.getClient();
	private Game					game			= this.client.getGame();
	
	// Отображение игры
	private ArrayList<GameViewPart>	gameViewParts	= new ArrayList<GameViewPart>();
	private GameViewPart			gameViewCell;
	private ZoneView				wayZoneView;
	private ZoneView				attackZoneView;
	private GameViewPart			gameViewCellDual;
	private GameViewPart			gameViewUnit;
	private GameViewPart			gameViewAction;
	private GameViewPart			gameViewCursor;
	
	private GestureDetector			gestureDetector;
	private Timer					timer;
	private Handler					updateHandler;
	
	public int						offsetX			= 0;
	public int						offsetY			= 0;
	
	public int						lastTapI;
	public int						lastTapJ;
	
	public boolean					isWayVisible	= false;
	public boolean					isAttackVisible	= false;
	
	public GameView(Context context)
	{
		super(context);
		
		setWillNotDraw(false);
		setClipChildren(false);
		
		this.gameViewCell = new GameViewCell(getContext(), this)
				.setDual(false).setField(this.game.map.getField());
		this.wayZoneView = new ZoneView(getContext());
		this.attackZoneView = new ZoneView(getContext());
		this.gameViewCellDual = new GameViewCell(getContext(), this)
				.setDual(true).setField(this.game.map.getField());
		
		this.gameViewUnit = new GameViewUnit(getContext(), this)
				.setField(this.game.fieldUnits)
				.setWayView(this.wayZoneView)
				.setAttackView(this.attackZoneView);
		this.gameViewCursor = new GameViewCursor(getContext(), this);
		this.gameViewAction = new GameViewAction(getContext(), this);
		
		addView(this.gameViewCell);
		addView(this.wayZoneView);
		addView(this.attackZoneView);
		addView(this.gameViewCellDual);
		addView(this.gameViewUnit);
		addView(this.gameViewAction);
		addView(this.gameViewCursor);
		
		this.gameViewParts.add(this.gameViewCell);
		this.gameViewParts.add(this.gameViewUnit);
		this.gameViewParts.add(this.gameViewCursor);
		this.gameViewParts.add(this.gameViewCellDual);
		
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
				Log.d("AE", "up");
				
				float y = event.getY() - GameView.this.offsetY;
				float x = event.getX() - GameView.this.offsetX;
				
				int i = (int) y / GameView.baseH;
				int j = (int) x / GameView.baseW;
				
				if (y < 0)
					i--;
				if (x < 0)
					j--;
				
				tap(i, j);
				return true;
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
			{
				GameView.this.offsetY -= distanceY;
				GameView.this.offsetX -= distanceX;
				
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
				GameView.this.updateHandler.sendMessage(new Message());
			}
		}, 0, GameView.DELAY_BETWEEN_UPDATES);
		
	}
	
	private void updateOffset()
	{
		for (GameViewPart gameViewPart : this.gameViewParts)
		{
			gameViewPart.updateOffset();
			gameViewPart.invalidate();
		}
	}
	
	private void updateCells()
	{
		SomeWithBitmaps.ordinal++;
		for (GameViewPart gameViewPart : this.gameViewParts)
			gameViewPart.invalidate();
		invalidate();
	}
	
	private void tap(int i, int j)
	{
		if (i == this.lastTapI && j == this.lastTapJ)
			return;
		
		this.lastTapI = i;
		this.lastTapJ = j;
		
		final Map map = this.game.map;
		
		if (!map.validateCoord(i, j))
			return;
		
		for (int k = this.gameViewParts.size() - 1; k >= 0; k--)
		{
			GameViewPart gameViewPart = this.gameViewParts.get(k);
			if (gameViewPart.update())
				break;
		}
		GameView.this.gameViewAction.update();
		
		invalidate();
	}
	
	public void performAction(ActionType actionType)
	{
		MyLog.log(actionType);
		
		this.isWayVisible = false;
		this.isAttackVisible = false;
		
		if (actionType == ActionType.ACTION_UNIT_MOVE)
		{
			this.isWayVisible = true;
			this.gameViewUnit.update();
		}
		else if (actionType == ActionType.ACTION_UNIT_REPAIR || actionType == ActionType.ACTION_UNIT_CAPTURE)
		{
			Action action = new Action(actionType);
			action.setProperty("i", this.lastTapI);
			action.setProperty("j", this.lastTapJ);
			ActionResult actionResult = Client.action(action);
			
			this.gameViewCell.update();
		}
		else if (actionType == ActionType.ACTION_UNIT_ATTACK)
		{
			this.isAttackVisible = true;
			this.gameViewUnit.update();
		}
		else if (actionType == ActionType.ACTION_CELL_BUY)
			this.gameActivity.startUnitBuyActivity();
		else if (actionType == ActionType.ACTION_END_TURN)
		{
			Action action = new Action(actionType);
			ActionResult actionResult = Client.action(action);
			
			this.gameViewUnit.update();
			
			Toast.makeText(getContext(), "Новый Ход!", Toast.LENGTH_SHORT).show();
		}
		this.gameViewAction.update();
	}
	
	public void performActionBuy(UnitType type)
	{
		Action action = new Action(ActionType.ACTION_CELL_BUY);
		action.setProperty("i", this.lastTapI);
		action.setProperty("j", this.lastTapJ);
		action.setProperty("type", type.ordinal);
		Client.action(action);
		
		this.gameViewCell.update();
		this.gameViewAction.update();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		Log.d("AE", "" + e.getAction());
		if (this.gestureDetector.onTouchEvent(e))
		{
			invalidate();
			return true;
		}
		else
			return performClick();
		// else return super.onTouchEvent(e);
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
		
		int actionRectH = (int) (0.2f * h);
		this.gameViewAction.setY(h - actionRectH);
		this.gameViewAction.setX(0);
		LayoutParams actionViewLayoutParams = new LayoutParams(w, actionRectH);
		this.gameViewAction.setLayoutParams(actionViewLayoutParams);
	}
	
}
