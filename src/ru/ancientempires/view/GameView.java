package ru.ancientempires.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ru.ancientempires.Cursor;
import ru.ancientempires.SomeWithBitmaps;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.ActionImages;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.Images;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Map;
import ru.ancientempires.model.Point;
import ru.ancientempires.model.PointWay;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.Way;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GameView extends FrameLayout
{
	// Модель игры
	private Client					client					= Client.getClient();
	
	// Отображение игры
	private static Cursor			cursor					= new Cursor();
	
	private Bitmap[][]				bitmaps;
	private GameActionView			gameActionView			= new GameActionView(getContext());
	
	private static SomeWithBitmaps	cursorWay				= new SomeWithBitmaps();
	private static SomeWithBitmaps	cursorAttack			= new SomeWithBitmaps();
	private static SomeWithBitmaps	cursorPointer			= new SomeWithBitmaps();
	
	private GestureDetector			gestureDetector;
	
	public static boolean			isBaseCellSizeDetermine	= false;
	public static int				baseH;
	public static int				baseW;
	public static float				baseHMulti				= 6 / 3.0f;
	public static float				baseWMulti				= GameView.baseHMulti;
	
	private int						offsetX					= 0;
	private int						offsetY					= 0;
	
	private float					difX;
	private float					difY;
	
	private boolean					isWayVisible			= false;
	private boolean					isAttackVisible			= false;
	
	private CircleView				circleViewWay			= new CircleView(getContext());
	private int						radiusAttack;
	private Bitmap					pointWaysBitmapStart;
	private Bitmap					pointWaysBitmap;
	private int						pointWayRadius;
	
	private int						startWayY;
	private int						startWayX;
	
	private PointWay[]				pointWays;
	private Point[]					attackedPoints;
	private Point[]					realAttackedPoints;
	private Point					currentAttackedPoint;
	
	private int						lastTapI;
	private int						lastTapJ;
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
		
		initBitmaps();
		
		setFocusable(true);
		setWillNotDraw(false);
		setClipChildren(false);
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
		
		addView(this.gameActionView);
		this.gameActionView.setAlpha(1);
		this.gameActionView.gameView = this;
		
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
			public boolean onSingleTapUp(MotionEvent e)
			{
				Log.d("AE", "up");
				
				float y = e.getY() - GameView.this.offsetY;
				float x = e.getX() - GameView.this.offsetX;
				
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
		GameView.this.circleViewWay.setY(this.offsetY + (this.lastUnitI - this.radiusAttack) * GameView.baseH);
		GameView.this.circleViewWay.setX(this.offsetX + (this.lastUnitJ - this.radiusAttack) * GameView.baseW);
	}
	
	public static void initResources(Resources res) throws IOException
	{
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
		GameActionView.initResources();
	}
	
	public static void startGame(Game game) throws IOException
	{
		Images.loadResources(Client.imagesZipFile, game);
	}
	
	public void initBitmaps()
	{
		Map map = this.client.getGame().map;
		this.bitmaps = new Bitmap[map.getHeight()][map.getWidth()];
		updateBitmaps();
	}
	
	private void updateBitmaps()
	{
		Map map = this.client.getGame().map;
		final int height = map.getHeight();
		final int width = map.getWidth();
		final Cell[][] field = map.getField();
		
		MyAssert.a(height == this.bitmaps.length);
		MyAssert.a(width == this.bitmaps[0].length);
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				this.bitmaps[i][j] = CellImages.getCellBitmap(field[i][j]);
		
		invalidate();
	}
	
	protected void updateCells()
	{
		SomeWithBitmaps.ordinal++;
		invalidate();
	}
	
	private void tap(int i, int j)
	{
		this.lastTapI = i;
		this.lastTapJ = j;
		
		final Game game = this.client.getGame();
		final Map map = game.map;
		
		if (!map.validateCoord(i, j))
			return;
		updateCursorState(i, j);
		
		boolean isAction = false;
		if (this.isWayVisible)
			isAction |= tryUnitMove(i, j, game);
		else if (this.isAttackVisible)
			isAction |= tryUnitAttack(i, j, game);
		if (!isAction || true)
			showActions(i, j);
		
		invalidate();
	}
	
	private void updateCursorState(int i, int j)
	{
		GameView.cursor.i = i;
		GameView.cursor.j = j;
		
		if (!GameView.cursor.isVisible)
			GameView.cursor.isVisible = true;
	}
	
	private boolean tryUnitMove(int i, int j, final Game game)
	{
		boolean pointWaysContains = false;
		for (final PointWay tempPointWay : this.pointWays)
			if (tempPointWay.i == i && tempPointWay.j == j)
			{
				pointWaysContains = true;
				break;
			}
		if (pointWaysContains)
		{
			this.isWayVisible = false;
			removeView(this.circleViewWay);
			
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
			// MyLog.log(actionType, " ", actionBitmap, " ");
			actionBitmaps.add(actionBitmap);
		}
		this.gameActionView.updateActionBitmapsState(actionBitmaps, actionTypes);
	}
	
	private void showNewUnitWay(int i, int j)
	{
		this.isWayVisible = true;
		this.lastUnitI = i;
		this.lastUnitJ = j;
		
		Action actionGetWay = new Action(ActionType.GET_UNIT_WAY);
		actionGetWay.setProperty("i", i);
		actionGetWay.setProperty("j", j);
		
		final long s1 = System.nanoTime();
		
		final ActionResult result = Client.action(actionGetWay);
		final Way way = (Way) result.getProperty("way");
		// MyLog.log(way.maxLength);
		
		this.pointWays = way.allPointWays.toArray(new PointWay[0]);
		
		// this.pointWaysBitmapStart = createPointsWaysBitmap(way);
		this.radiusAttack = way.maxLength - 1;
		this.circleViewWay = new CircleView(getContext());
		
		Point[] points = (Point[]) result.getProperty("allPoints");
		this.circleViewWay.setView(new ZoneView(getContext()).setRadius(this.radiusAttack)
				.setZone(points).setBitmap(GameView.cursorWay.getBitmap()));
		
		addView(this.circleViewWay);
		
		this.circleViewWay.setRadius(this.radiusAttack * GameView.baseH);
		this.circleViewWay.setCenter((this.radiusAttack + 0.5f) * GameView.baseH, (this.radiusAttack + 0.5f) * GameView.baseW);
		updateOffset();
		
		ValueAnimator animator = ValueAnimator.ofInt(0, way.maxLength * GameView.baseH);
		animator.setDuration(2000);
		MyLog.log(ValueAnimator.getFrameDelay());
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				// GameView.this.pointWayRadius = (int) animation.getAnimatedValue();
				// updatePointWaysBitmap(GameView.this.pointWayRadius);
			}
		});
		animator.start();
		
		if (false)
		{
			final long s2 = System.nanoTime();
			Log.e("ae", way.toString());
			final long e = System.nanoTime();
			final String text = String.format("%s мс из %s мс", (e - s2) / 1000000.0d, (e - s1) / 1000000.0d);
			Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
		}
	}
	
	private Bitmap createPointsWaysBitmap(Way way)
	{
		int radius = way.maxLength - 1;
		int startI = way.pointWayStart.i - radius;
		int startJ = way.pointWayStart.j - radius;
		
		Bitmap bitmap = Bitmap.createBitmap((radius * 2 + 1) * GameView.baseW,
				(radius * 2 + 1) * GameView.baseH, Config.ARGB_8888);
		Canvas bitmapCanvas = new Canvas(bitmap);
		for (PointWay pointWay : this.pointWays)
		{
			int relI = pointWay.i - startI;
			int relJ = pointWay.j - startJ;
			bitmapCanvas.drawBitmap(GameView.cursorWay.getBitmap(), relJ * GameView.baseW,
					relI * GameView.baseH, null);
		}
		
		this.startWayY = startI * GameView.baseH;
		this.startWayX = startJ * GameView.baseW;
		
		return bitmap;
	}
	
	Paint	clearPaint	= new Paint();
	
	Bitmap	circleBitmap;
	
	private void updatePointWaysBitmap(int radius)
	{
		// this.maskPaint.setColor(Color.GREEN);
		this.clearPaint.setColor(Color.TRANSPARENT);
		this.clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		this.clearPaint.setStyle(Paint.Style.FILL);
		
		int height = this.pointWaysBitmapStart.getHeight();
		int width = this.pointWaysBitmapStart.getWidth();
		if (this.circleBitmap == null)
			this.circleBitmap = Bitmap.createBitmap(width, height, Config.ALPHA_8);
		Canvas circleCanvas = new Canvas(this.circleBitmap);
		
		Paint blackPaint = new Paint();
		blackPaint.setColor(Color.BLACK);
		
		circleCanvas.drawRect(0, 0, width, height, blackPaint);
		circleCanvas.drawCircle(width / 2, height / 2, radius, this.clearPaint);
		
		if (this.pointWaysBitmap == null)
			this.pointWaysBitmap = Bitmap.createBitmap(this.pointWaysBitmapStart);
		else
		{
			Canvas canvas = new Canvas(this.pointWaysBitmap);
			canvas.drawRect(0, 0, width, height, this.clearPaint);
			canvas.drawBitmap(this.pointWaysBitmapStart, 0, 0, null);
		}
		Canvas bitmapCanvas = new Canvas(this.pointWaysBitmap);
		bitmapCanvas.drawBitmap(this.circleBitmap, 0, 0, this.clearPaint);
	}
	
	public void performAction(ActionType actionType)
	{
		MyLog.log(actionType);
		
		this.isWayVisible = false;
		removeView(this.circleViewWay);
		this.isAttackVisible = false;
		
		if (actionType == ActionType.ACTION_UNIT_REPAIR || actionType == ActionType.ACTION_UNIT_CAPTURE)
		{
			Action action = new Action(actionType);
			action.setProperty("i", this.lastTapI);
			action.setProperty("j", this.lastTapJ);
			ActionResult actionResult = Client.action(action);
		}
		else if (actionType == ActionType.ACTION_UNIT_ATTACK)
			showNewAttackZone(this.lastTapI, this.lastTapJ);
		else if (actionType == ActionType.ACTION_END_TURN)
		{
			Action action = new Action(actionType);
			ActionResult actionResult = Client.action(action);
			Toast.makeText(getContext(), "Новый Ход!", Toast.LENGTH_SHORT).show();
		}
		showActions(this.lastTapI, this.lastTapJ);
		
		updateBitmaps();
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
		this.gameActionView.setY(h - this.actionRectHeight);
		this.gameActionView.setX(0);
		FrameLayout.LayoutParams actionViewLayoutParams = new FrameLayout.LayoutParams(w, this.actionRectHeight);
		this.gameActionView.setLayoutParams(actionViewLayoutParams);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// рисуем игровое поле
		final Game game = this.client.getGame();
		
		// карта
		final Cell[][] field = game.map.getField();
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
			{
				final Bitmap bitmapCell = this.bitmaps[i][j];
				final int y = GameView.baseH * i + this.offsetY;
				final int x = GameView.baseW * j + this.offsetX;
				canvas.drawBitmap(bitmapCell, x, y, null);
			}
		
		if (this.isWayVisible && false)
		{
			final Bitmap bitmapCursorWay = GameView.cursorWay.getBitmap();
			final int height = bitmapCursorWay.getHeight();
			final int width = bitmapCursorWay.getWidth();
			for (PointWay pointWay : this.pointWays)
			{
				final int y = GameView.baseH * pointWay.i - (height - GameView.baseH) / 2 + this.offsetY;
				final int x = GameView.baseW * pointWay.j - (width - GameView.baseW) / 2 + this.offsetX;
				canvas.drawBitmap(bitmapCursorWay, x, y, null);
			}
		}
		
		if (this.isWayVisible && false)
			canvas.drawBitmap(this.pointWaysBitmap, this.startWayX + this.offsetX, this.startWayY + this.offsetY, null);
		
		if (this.isAttackVisible)
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
		
		// юниты
		final Unit[][] fieldUnits = game.fieldUnits;
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
		
		if (this.isAttackVisible)
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
		
		// рисуем курсор (если есть)
		if (GameView.cursor.isVisible)
		{
			final Bitmap bitmapCursor = GameView.cursor.getBitmap();
			final int y = GameView.baseH * GameView.cursor.i - (bitmapCursor.getHeight() - GameView.baseH) / 2 + this.offsetY;
			final int x = GameView.baseW * GameView.cursor.j - (bitmapCursor.getWidth() - GameView.baseW) / 2 + this.offsetX;
			canvas.drawBitmap(bitmapCursor, x, y, null);
		}
		
	}
}
