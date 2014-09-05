package ru.ancientempires;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Map;
import ru.ancientempires.model.Point;
import ru.ancientempires.model.PointWay;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.Way;
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
import android.widget.Toast;

public class GameView extends View
{
	// Модель игры
	private Client				client					= Client.getClient();
	
	// Отображение игры
	private Cursor				cursor					= new Cursor();
	
	private Bitmap[][]			bitmaps;
	
	private SomeWithBitmaps		cursorWay				= new SomeWithBitmaps();
	
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
	
	public GameView(Context context) throws IOException
	{
		super(context);
		
		initResources(getResources());
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
	
	public void initResources(Resources res) throws IOException
	{
		this.cursor.setBitmaps(new Bitmap[]
		{
				BitmapHelper.getBitmap(Client.imagesZipFile, "cursor_down.png"),
				BitmapHelper.getBitmap(Client.imagesZipFile, "cursor_up.png")
		});
		this.cursorWay.setBitmaps(new Bitmap[]
		{
				BitmapHelper.getBitmap(Client.imagesZipFile, "cursor_way.png")
		});
	}
	
	public static void startGame(Game game) throws IOException
	{
		Images.loadResources(Client.imagesZipFile, game);
	}
	
	public void initBitmaps(Map map)
	{
		final int height = map.getHeight();
		final int width = map.getWidth();
		final Cell[][] field = map.getField();
		
		this.bitmaps = new Bitmap[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				this.bitmaps[i][j] = Images.getCellBitmap(field[i][j]);
	}
	
	public void updateBitmaps(Map map)
	{
		final int height = map.getHeight();
		final int width = map.getWidth();
		final Cell[][] field = map.getField();
		
		assert height == this.bitmaps.length;
		assert width == this.bitmaps[0].length;
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				Cell cell = field[i][j];
				if (!cell.type.isStatic())
					this.bitmaps[i][j] = Images.getCellBitmap(cell);
			}
	}
	
	protected void updateCells()
	{
		SomeWithBitmaps.ordinal++;
		invalidate();
	}
	
	private void tap(int i, int j)
	{
		final Game game = this.client.getGame();
		final Map map = game.map;
		
		if (!map.validateCoord(new Point(i, j)))
			return;
		this.cursor.i = i;
		this.cursor.j = j;
		
		if (!this.cursor.isVisible)
			this.cursor.isVisible = true;
		
		Unit[][] fieldUnits = game.fieldUnits;
		Unit tapUnit = fieldUnits[i][j];
		if (tapUnit != null)
		{
			this.isWayVisible = true;
			this.unitI = i;
			this.unitJ = j;
			
			int rep = 100;
			
			long t2 = 0;
			for (int k = 0; k < rep; k++)
			{
				long s = System.nanoTime();
				final Way way = this.client.getWay(this.cursor.i, this.cursor.j, tapUnit);
				long e = System.nanoTime();
				t2 += e - s;
			}
			
			long t1 = 0;
			for (int k = 0; k < rep; k++)
			{
				long s = System.nanoTime();
				Action action = new Action(ActionType.GET_MOVE_UNIT_WAY);
				action.setProperty("i", i);
				action.setProperty("j", j);
				ActionResult result = Client.action(action);
				final Way way = (Way) result.getProperty("way");
				long e = System.nanoTime();
				t1 += e - s;
			}
			
			t1 /= rep;
			t2 /= rep;
			
			String text = String.format("1: %s мс, 2: %s мс", t1 / 1000000.0f, t2 / 1000000.0f);
			Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
			
			Action action = new Action(ActionType.GET_MOVE_UNIT_WAY);
			action.setProperty("i", i);
			action.setProperty("j", j);
			ActionResult result = Client.action(action);
			final Way way = (Way) result.getProperty("way");
			
			this.pointWays = way.allPointWays.toArray(new PointWay[0]);
			
			Log.e("ae", way.toString());
		}
		else if (this.isWayVisible)
		{
			boolean pointWaysContains = false;
			for (PointWay tempPointWay : this.pointWays)
				if (tempPointWay.i == i && tempPointWay.j == j)
				{
					pointWaysContains = true;
					break;
				}
			if (pointWaysContains)
			{
				this.isWayVisible = false;
				
				Action action = new Action(ActionType.ACTION_MOVE_UNIT);
				action.setProperty("oldI", this.unitI);
				action.setProperty("oldJ", this.unitJ);
				action.setProperty("newI", i);
				action.setProperty("newJ", j);
				
				Client.action(action);
				
				String text = String.format("Юнит %s сходил c (%s, %s) на (%s, %s)",
						fieldUnits[i][j].type.name, this.unitI, this.unitJ, i, j);
				// Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
			}
		}
		else
			this.isWayVisible = false;
		
		invalidate();
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
					final Unit unit = fieldUnits[i][j];
					final Bitmap bitmapUnit = Images.getUnitBitmap(unit);
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
