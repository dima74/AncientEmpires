package ru.ancientempires.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ru.ancientempires.SomeWithBitmaps;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.ActionImages;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Map;
import ru.ancientempires.model.Point;
import ru.ancientempires.model.Unit;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GameView extends FrameLayout
{
	// Модель игры
	private Client					client					= Client.getClient();
	public Game						game					= this.client.getGame();
	
	// Отображение игры
	// public static Cursor cursor = new Cursor();
	
	protected Bitmap[][]			bitmaps;
	protected int[][]				offsetI, offsetJ;
	
	private ArrayList<GameViewPart>	gameViewParts			= new ArrayList<GameViewPart>();
	private GameViewPart			gameViewCell;
	private ZoneView				wayZoneView;
	private ZoneView				attackZoneView;
	private GameViewPart			gameViewUnit;
	private GameViewPart			gameViewAction;
	private GameViewPart			gameViewCursor;
	
	private static SomeWithBitmaps	cursorWay				= new SomeWithBitmaps();
	private static SomeWithBitmaps	cursorAttack			= new SomeWithBitmaps();
	private static SomeWithBitmaps	cursorPointer			= new SomeWithBitmaps();
	
	private GestureDetector			gestureDetector;
	
	public static boolean			isBaseCellSizeDetermine	= false;
	public static int				baseH;
	public static int				baseW;
	public static float				baseHMulti				= 6 / 3.0f;
	public static float				baseWMulti				= GameView.baseHMulti;
	
	public int						offsetX					= 0;
	public int						offsetY					= 0;
	
	private float					difX;
	private float					difY;
	
	public boolean					isWayVisible			= false;
	public boolean					isAttackVisible			= false;
	
	private ZoneView				zoneView				= new ZoneView(getContext());
	private int						radiusAttack;
	
	boolean[][]						fieldCellWays;
	private Point[]					attackedPoints;
	private Point[]					realAttackedPoints;
	private Point					currentAttackedPoint;
	
	public int						lastTapI;
	public int						lastTapJ;
	private int						lastUnitI;
	private int						lastUnitJ;
	
	private Timer					timer;
	protected Handler				updateHandler			= new Handler(new Handler.Callback()
															{
																@Override
																public boolean handleMessage(Message msg)
																{
																	updateCells();
																	return true;
																}
															});
	
	// не знаю, как выводится эта константа
	private static final int		DELAY_BETWEEN_UPDATES	= 265;
	
	public GameView(Context context)
	{
		super(context);
		
		// initBitmaps();
		
		setWillNotDraw(false);
		setClipChildren(false);
		
		this.gameViewCell = new GameViewCell(getContext(), this).setField(this.game.map.getField());
		this.wayZoneView = new ZoneView(getContext());
		this.attackZoneView = new ZoneView(getContext());
		this.gameViewUnit = new GameViewUnit(getContext(), this)
				.setField(this.game.fieldUnits)
				.setWayView(this.wayZoneView)
				.setAttackView(this.attackZoneView);
		this.gameViewCursor = new GameViewCursor(getContext(), this);
		this.gameViewAction = new GameViewAction(getContext(), this);
		
		addView(this.gameViewCell);
		addView(this.wayZoneView);
		addView(this.attackZoneView);
		addView(this.gameViewUnit);
		addView(this.gameViewAction);
		addView(this.gameViewCursor);
		
		this.gameViewParts.add(this.gameViewCell);
		this.gameViewParts.add(this.gameViewUnit);
		this.gameViewParts.add(this.gameViewCursor);
		
		this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDown(MotionEvent e)
			{
				return true;
			}
			
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e)
			{
				Log.d("AE", "conf");
				return super.onSingleTapConfirmed(e);
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
				GameView.this.difY = -distanceY;
				GameView.this.difX = -distanceX;
				
				GameView.this.offsetY += GameView.this.difY;
				GameView.this.offsetX += GameView.this.difX;
				
				updateOffset();
				
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
	
	protected void updateOffset()
	{
		// this.zoneView.setY(this.offsetY + (this.lastUnitI - this.radiusAttack) * GameView.baseH);
		// this.zoneView.setX(this.offsetX + (this.lastUnitJ - this.radiusAttack) * GameView.baseW);
		
		for (GameViewPart gameViewPart : this.gameViewParts)
		{
			// gameViewPart.offsetX = this.offsetX;
			// gameViewPart.offsetY = this.offsetY;
			gameViewPart.updateOffset();
			gameViewPart.invalidate();
		}
	}
	
	public static void initResources(Resources res) throws IOException
	{
		GameViewCursor.init();
		GameViewUnit.init();
		GameViewAction.initResources();
		/*
		GameView.cursor.setBitmaps(new String[]
		{
				"cursor_down.png",
				"cursor_up.png"
		});
		GameView.cursorWay.setBitmaps(new String[]
		{
				"cursor_way.png"
		});
		GameView.cursorAttack.setBitmaps(new String[]
		{
				"cursor_attack.png"
		});
		GameView.cursorPointer.setBitmaps(new String[]
		{
				"cursor_pointer_0.png",
				"cursor_pointer_1.png",
				"cursor_pointer_2.png"
		});
		*/
	}
	
	/*
	public void initBitmaps1()
	{
		Map map = this.client.getGame().map;
		this.bitmaps = new Bitmap[map.getHeight()][map.getWidth()];
		this.offsetI = new int[map.getHeight()][map.getWidth()];
		this.offsetJ = new int[map.getHeight()][map.getWidth()];
		updateBitmaps1();
	}
	*/
	
	/*
	private void updateBitmaps1()
	{
		Map map = this.client.getGame().map;
		final int height = map.getHeight();
		final int width = map.getWidth();
		final Cell[][] field = map.getField();
		
		MyAssert.a(height == this.bitmaps.length);
		MyAssert.a(width == this.bitmaps[0].length);
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				Cell cell = field[i][j];
				CellBitmap cellBitmap = CellImages.cellsBitmaps[cell.type.ordinal];
				this.bitmaps[i][j] = cellBitmap.getBitmap(cell);
				this.offsetI[i][j] = cellBitmap.offsetI;
				this.offsetJ[i][j] = cellBitmap.offsetJ;
			}
		
		invalidate();
	}
	*/
	
	protected void updateCells()
	{
		SomeWithBitmaps.ordinal++;
		for (GameViewPart gameViewPart : this.gameViewParts)
			gameViewPart.invalidate();
		invalidate();
	}
	
	protected void tap(int i, int j)
	{
		if (i == this.lastTapI && j == this.lastTapJ)
			return;
		
		this.lastTapI = i;
		this.lastTapJ = j;
		
		final Map map = this.game.map;
		
		if (!map.validateCoord(i, j))
			return;
		// updateCursorState(i, j);
		
		boolean isAction = false;
		if (this.isWayVisible && false)
			isAction |= tryUnitMove(i, j, this.game);
		else if (this.isAttackVisible && false)
			isAction |= tryUnitAttack(i, j, this.game);
		if (!isAction && false)
			showActions(i, j);
		
		for (int k = this.gameViewParts.size() - 1; k >= 0; k--)
		{
			GameViewPart gameViewPart = this.gameViewParts.get(k);
			if (gameViewPart.update())
				break;
		}
		GameView.this.gameViewAction.update();
		
		invalidate();
	}
	
	/*
	private void updateCursorState(int i, int j)
	{
		GameView.cursor.i = i;
		GameView.cursor.j = j;
		
		if (!GameView.cursor.isVisible)
			GameView.cursor.isVisible = true;
	}
	*/
	
	private boolean tryUnitMove(int i, int j, final Game game)
	{
		boolean pointWaysContains = this.fieldCellWays
									[this.radiusAttack + i - this.lastUnitI]
									[this.radiusAttack + j - this.lastUnitJ];
		
		if (pointWaysContains)
		{
			this.isWayVisible = false;
			removeView(this.zoneView);
			
			final Action action = new Action(ActionType.ACTION_UNIT_MOVE);
			action.setProperty("oldI", this.lastUnitI);
			action.setProperty("oldJ", this.lastUnitJ);
			action.setProperty("newI", i);
			action.setProperty("newJ", j);
			
			Client.action(action);
			
			if (false)
			{
				final Unit[][] fieldUnits = game.fieldUnits;
				final String text = String.format("Юнит %s сходил c (%s, %s) на (%s, %s)",
						fieldUnits[i][j].type.name, this.lastUnitI, this.lastUnitJ, i, j);
				Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
			}
			
			return true;
		}
		else
		{
			this.isWayVisible = false;
			removeView(this.zoneView);
			return false;
		}
	}
	
	private boolean tryUnitAttack(int i, int j, final Game game)
	{
		final Unit[][] fieldUnits = game.fieldUnits;
		boolean pointAttackContains = false;
		for (final Point tempPoint : this.attackedPoints)
			if (tempPoint.i == i && tempPoint.j == j)
			{
				pointAttackContains = true;
				break;
			}
		if (pointAttackContains)
		{
			this.isAttackVisible = false;
			
			final Action action = new Action(ActionType.ACTION_UNIT_ATTACK);
			action.setProperty("attackingI", this.lastUnitI);
			action.setProperty("attackingJ", this.lastUnitJ);
			action.setProperty("attackedI", i);
			action.setProperty("attackedJ", j);
			
			Client.action(action);
			
			if (true)
			{
				final String text = String.format("Юнит %s (%s, %s) атаковал (%s, %s)",
						fieldUnits[i][j].type.name, this.lastUnitI, this.lastUnitJ, i, j);
				Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
			}
			
			return true;
		}
		else
		{
			this.isAttackVisible = false;
			return false;
		}
	}
	
	private void showActions(int i, int j)
	{
		Action action = new Action(ActionType.GET_AVAILABLE_ACTIONS_FOR_CELL);
		action.setProperty("i", i);
		action.setProperty("j", j);
		
		final ActionResult result = Client.action(action);
		
		final int amount = (Integer) result.getProperty("amountActions");
		ArrayList<ActionType> actionTypes = new ArrayList<ActionType>(amount);
		for (int k = 0; k < amount; k++)
		{
			final ActionType actionType = (ActionType) result.getProperty("action" + k);
			actionTypes.add(actionType);
		}
		
		if (actionTypes.contains(ActionType.ACTION_UNIT_MOVE))
			showNewUnitWay(i, j);
		
		ArrayList<Bitmap> actionBitmaps = new ArrayList<Bitmap>(amount);
		for (ActionType actionType : actionTypes)
		{
			Bitmap actionBitmap = ActionImages.getActionBitmap(actionType);
			actionBitmaps.add(actionBitmap);
		}
		// this.gameViewAction.updateActionBitmapsState(actionBitmaps, actionTypes);
	}
	
	public void showNewUnitWay(int i, int j)
	{
		this.isWayVisible = true;
		this.lastUnitI = i;
		this.lastUnitJ = j;
		
		Action actionGetWay = new Action(ActionType.GET_UNIT_WAY);
		actionGetWay.setProperty("i", i);
		actionGetWay.setProperty("j", j);
		
		final long s1 = System.nanoTime();
		
		new AsyncTask<Action, Void, ActionResult>()
		{
			@Override
			protected ActionResult doInBackground(Action... params)
			{
				return Client.action(params[0]);
			}
			
			@Override
			protected void onPostExecute(ActionResult result)
			{
				/*
				final Way way = (Way) result.getProperty("way");
				
				GameView.this.pointWays = way.allPointWays.toArray(new PointWay[0]);
				GameView.this.radiusAttack = way.maxLength - 1;
				
				Point[] points = (Point[]) result.getProperty("allPoints");
				*/
				
				// int[][] field = (int[][]) result.getProperty("field");
				
				GameView.this.fieldCellWays = (boolean[][]) result.getProperty("is");
				GameView.this.radiusAttack = GameView.this.fieldCellWays.length / 2;
				
				GameView.this.zoneView = new ZoneView(getContext())
						.setBitmap(GameView.cursorWay.getBitmap())
						.setRadius(GameView.this.radiusAttack)
						.setZone(GameView.this.fieldCellWays);
				addView(GameView.this.zoneView);
				GameView.this.zoneView.startAnimate();
				
				updateOffset();
			}
		}.execute(actionGetWay);
		
		/*
		final ActionResult result = Client.action(actionGetWay);
		final Way way = (Way) result.getProperty("way");
		
		this.pointWays = way.allPointWays.toArray(new PointWay[0]);
		this.radiusAttack = way.maxLength - 1;
		
		Point[] points = (Point[]) result.getProperty("allPoints");
		
		this.zoneView = new ZoneView(getContext())
				.setBitmap(GameView.cursorWay.getBitmap())
				.setRadius(this.radiusAttack)
				.setZone(points);
		addView(this.zoneView);
		this.zoneView.startAnimate();
		
		updateOffset();
		*/
		
		if (false)
		{
			final long s2 = System.nanoTime();
			// Log.e("ae", way.toString());
			final long e = System.nanoTime();
			final String text = String.format("%s мс из %s мс", (e - s2) / 1000000.0d, (e - s1) / 1000000.0d);
			Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void performAction(ActionType actionType)
	{
		MyLog.log(actionType);
		
		this.isWayVisible = false;
		this.isAttackVisible = false;
		// removeView(this.zoneView);
		
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
			// showNewAttackZone(this.lastTapI, this.lastTapJ);
			this.isAttackVisible = true;
			this.gameViewUnit.update();
		}
		else if (actionType == ActionType.ACTION_END_TURN)
		{
			Action action = new Action(actionType);
			ActionResult actionResult = Client.action(action);
			Toast.makeText(getContext(), "Новый Ход!", Toast.LENGTH_SHORT).show();
		}
		// showActions(this.lastTapI, this.lastTapJ);
		this.gameViewAction.update();
		// updateBitmaps();
	}
	
	private void showNewAttackZone(int i, int j)
	{
		this.isAttackVisible = true;
		this.lastUnitI = i;
		this.lastUnitJ = j;
		
		Action action = new Action(ActionType.GET_UNIT_ATTACK);
		action.setProperty("i", i);
		action.setProperty("j", j);
		
		final ActionResult result = Client.action(action);
		this.attackedPoints = (Point[]) result.getProperty("allAttackedPoints");
		this.realAttackedPoints = (Point[]) result.getProperty("realAttackedPoints");
		this.currentAttackedPoint = this.realAttackedPoints[0];
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
	
	public int	height;
	public int	width;
	
	public int	actionRectHeight;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		this.height = h;
		this.width = w;
		
		this.actionRectHeight = (int) (0.2f * h);
		this.gameViewAction.setY(h - this.actionRectHeight);
		this.gameViewAction.setX(0);
		FrameLayout.LayoutParams actionViewLayoutParams = new FrameLayout.LayoutParams(w, this.actionRectHeight);
		this.gameViewAction.setLayoutParams(actionViewLayoutParams);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// рисуем игровое поле
		
		// карта
		/*
		final Cell[][] field = this.game.map.getField();
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
			{
				final Bitmap bitmapCell = this.bitmaps[i][j];
				final int y = GameView.baseH * (i + this.offsetI[i][j]) + this.offsetY;
				final int x = GameView.baseW * (j + this.offsetJ[i][j]) + this.offsetX;
				canvas.drawBitmap(bitmapCell, x, y, null);
			}
		*/
		
		if (this.isAttackVisible && false)
		{
			final Bitmap bitmapCursorAttack = GameView.cursorAttack.getBitmap();
			final int height = bitmapCursorAttack.getHeight();
			final int width = bitmapCursorAttack.getWidth();
			for (Point point : this.attackedPoints)
			{
				final int y = GameView.baseH * point.i - (height - GameView.baseH) / 2 + this.offsetY;
				final int x = GameView.baseW * point.j - (width - GameView.baseW) / 2 + this.offsetX;
				canvas.drawBitmap(bitmapCursorAttack, x, y, null);
			}
		}
		
		/*
		// юниты
		final Unit[][] fieldUnits = this.game.fieldUnits;
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
			{
				final Unit unit = fieldUnits[i][j];
				if (unit != null)
				{
					final int y = GameView.baseH * i + this.offsetY;
					final int x = GameView.baseW * j + this.offsetX;
					
					final Bitmap bitmapUnit = UnitImages.getUnitBitmap(unit);
					canvas.drawBitmap(bitmapUnit, x, y, null);
					
					final int health = Math.round(unit.health * 100);
					
					final int one = health / 10;
					final int two = health % 10;
					
					final int textX = x;
					final int textY = y + GameView.baseH - NumberImages.height;
					
					if (one == 0)
						canvas.drawBitmap(NumberImages.getNumberBitmap(two), textX, textY, null);
					else
					{
						canvas.drawBitmap(NumberImages.getNumberBitmap(one), textX, textY, null);
						canvas.drawBitmap(NumberImages.getNumberBitmap(two), textX + NumberImages.width, textY, null);
					}
				}
			}
		*/
		
		if (this.isAttackVisible && false)
		{
			final Bitmap bitmapCursorPointer = GameView.cursorPointer.getBitmap();
			final int height = bitmapCursorPointer.getHeight();
			final int width = bitmapCursorPointer.getWidth();
			final int currAttackedI = this.currentAttackedPoint.i;
			final int currAttackedJ = this.currentAttackedPoint.j;
			final int y = GameView.baseH * currAttackedI - (height - GameView.baseH) / 2 + this.offsetY;
			final int x = GameView.baseW * currAttackedJ - (width - GameView.baseW) / 2 + this.offsetX;
			canvas.drawBitmap(bitmapCursorPointer, x, y, null);
		}
		
		/*
		// рисуем курсор (если есть)
		if (GameView.cursor.isVisible && false)
		{
			final Bitmap bitmapCursor = GameView.cursor.getBitmap();
			final int y = GameView.baseH * GameView.cursor.i - (bitmapCursor.getHeight() - GameView.baseH) / 2 + this.offsetY;
			final int x = GameView.baseW * GameView.cursor.j - (bitmapCursor.getWidth() - GameView.baseW) / 2 + this.offsetX;
			canvas.drawBitmap(bitmapCursor, x, y, null);
		}
		*/
	}
}
