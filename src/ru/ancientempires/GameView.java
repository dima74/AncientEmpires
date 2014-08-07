package ru.ancientempires;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import model.Cell;
import model.Game;
import model.Map;
import model.Point;
import model.PointWay;
import model.Unit;
import model.UnitType;
import model.Way;
import ru.ancientempires.helpers.ImageHelper;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import client.Client;

public class GameView extends View
{
	// Модель игры
	private Client				client					= Client.getClient();
	
	// Отображение игры
	private Cursor				cursor					= (Cursor) new Cursor()
																.setBitmaps(getResources(), new int[]
																{
			R.drawable.cursor_up, R.drawable.cursor_down
																});
	
	// private static Cell[] cells = new Cell[CellType1.values().length];
	private Bitmap[][]			bitmaps;
	
	private SomeWithBitmaps		cursorWay				= new SomeWithBitmaps().setBitmaps(getResources(), new int[]
														{
																R.drawable.cursor_way
														});
	
	private GestureDetector		gestureDetector;
	
	public static boolean		isBaseCellSizeDetermine	= false;
	public static int			baseCellHeight;
	public static int			baseCellWidth;
	public static float			baseCellHeightMulti		= (float) (4 / 3.0);
	public static float			baseCellWidthMulti		= GameView.baseCellHeightMulti;
	
	private int					offsetX					= 0;
	private int					offsetY					= 0;
	
	private float				difX;
	private float				difY;
	
	private boolean				isWayVisible			= false;
	private int					unitI;
	private int					unitJ;
	private PointWay[]			pointWays;
	
	private Timer				timer;
	protected Handler			updateHandler			= new Handler(new Handler.Callback()
														{
															@Override
															public boolean handleMessage(Message msg)
															{
																updateCells();
																return true;
															}
														});
	
	// не знаю, как выводится эта константа
	private static final int	DELAY_BETWEEN_UPDATES	= 265;
	private boolean				isOneBitmapLast			= false;
	
	public GameView(Context context)
	{
		super(context);
		
		this.client.startGame("first");
		initBitmaps(this.client.getGame().map);
		
		setFocusable(true);
		setWillNotDraw(false);
		
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
				
				int i = (int) y / GameView.baseCellHeight;
				int j = (int) x / GameView.baseCellWidth;
				
				if (y < 0) i--;
				if (x < 0) j--;
				
				tap(i, j);
				return true;
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
			{
				GameView.this.difX = -distanceX;
				GameView.this.difY = -distanceY;
				
				GameView.this.offsetX += GameView.this.difX;
				GameView.this.offsetY += GameView.this.difY;
				
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
	
	public static void initResources(Resources res) throws IOException
	{
		UnitDraw.initResorces(res);
		
		ImageLoader.initResources(Client.imagesZipFile);
	}
	
	public void initBitmaps(Map map)
	{
		this.bitmaps = new Bitmap[map.height][map.width];
		for (int i = 0; i < map.height; i++)
			for (int j = 0; j < map.width; j++)
				this.bitmaps[i][j] = ImageHelper.getBitmap(map.field[i][j]);
	}
	
	protected void updateCells()
	{
		SomeWithBitmaps.ordinal++;
		this.isOneBitmapLast = !this.isOneBitmapLast;
		invalidate();
	}
	
	private void tap(int i, int j)
	{
		final Game game = this.client.getGame();
		final Map map = game.map;
		
		if (!map.validateCellPoint(new Point(i, j))) return;
		this.cursor.i = i;
		this.cursor.j = j;
		
		if (!this.cursor.isVisible) this.cursor.isVisible = true;
		
		Unit[][] fieldUnits = game.fieldUnits;
		Unit unit;
		if ((unit = fieldUnits[i][j]) != null)
		{
			this.isWayVisible = true;
			this.unitI = i;
			this.unitJ = j;
			final Way way = this.client.getWay(this.cursor.i, this.cursor.j, unit);
			
			this.pointWays = way.allPointWays.toArray(new PointWay[0]);
			
			log(way.pointWayStart, "");
		}
		else if (this.isWayVisible)
		{
			boolean allPointWaysContains = false;
			for (PointWay tempPointWay : this.pointWays)
				if (tempPointWay.i == i && tempPointWay.j == j) allPointWaysContains = true;
			if (allPointWaysContains)
			{
				this.isWayVisible = false;
				Unit unit1 = fieldUnits[this.unitI][this.unitJ];
				fieldUnits[this.unitI][this.unitJ] = null;
				unit1.i = i;
				unit1.j = j;
				fieldUnits[i][j] = unit1;
			}
		}
		else this.isWayVisible = false;
		
		invalidate();
	}
	
	private void log(PointWay pointWay, String s)
	{
		Log.e("ae", s + pointWay.i + " " + pointWay.j);
		for (PointWay nextPointWay : pointWay.nextPointWays)
			log(nextPointWay, s + "->");
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
		else return performClick();
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
	protected void onDraw(Canvas canvas)
	{
		// рисуем игровое поле
		
		final Game game = this.client.getGame();
		
		final Cell[][] field = game.map.getField();
		final Unit[][] fieldUnits = game.fieldUnits;
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
			{
				// карта
				final Bitmap bitmapCell = this.bitmaps[i][j];
				final int y = bitmapCell.getHeight() * i + this.offsetY;
				final int x = bitmapCell.getWidth() * j + this.offsetX;
				canvas.drawBitmap(bitmapCell, x, y, null);
				
				// сверху юниты
				
				if (fieldUnits[i][j] != null)
				{
					Unit unit = fieldUnits[i][j];
					final UnitType unitType = unit.unitType;
					final UnitDraw unitDraw = UnitDraw.getUnitDraw(unitType);
					final Bitmap bitmapUnit = unitDraw.getBitmap();
					canvas.drawBitmap(bitmapUnit, x, y, null);
				}
				
			}
		
		// рисуем курсор (если есть)
		if (this.cursor.isVisible)
		{
			final Bitmap bitmapCursor = this.cursor.getBitmap();
			final int y = GameView.baseCellHeight * this.cursor.i - (bitmapCursor.getHeight() - GameView.baseCellHeight) / 2 + this.offsetY;
			final int x = GameView.baseCellWidth * this.cursor.j - (bitmapCursor.getWidth() - GameView.baseCellWidth) / 2 + this.offsetX;
			canvas.drawBitmap(bitmapCursor, x, y, null);
		}
		
		if (this.isWayVisible)
		{
			final Bitmap bitmapCursorWay = this.cursorWay.getBitmap();
			final int height = bitmapCursorWay.getHeight();
			final int width = bitmapCursorWay.getWidth();
			for (PointWay pointWay : this.pointWays)
			{
				final int y = GameView.baseCellHeight * pointWay.i - (height - GameView.baseCellHeight) / 2 + this.offsetY;
				final int x = GameView.baseCellWidth * pointWay.j - (width - GameView.baseCellWidth) / 2 + this.offsetX;
				canvas.drawBitmap(bitmapCursorWay, x, y, null);
			}
		}
		
	}
}
