package ru.ancientempires;

import java.util.Timer;
import java.util.TimerTask;

import model.Cell.CellType;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
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
																.setBitmapOne(getResources(), R.drawable.cursor_up)
																.setBitmapTwo(getResources(), R.drawable.cursor_down);
	
	private static Cell[]		cells					= new Cell[CellType.values().length];
	
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
		
		setFocusable(true);
		setWillNotDraw(false);
		
		// TODO выпилить
		this.cursor.i = 3;
		this.cursor.j = 5;
		
		// client.getClient().
		
		//
		
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
				int i = (int) (e.getY() - GameView.this.offsetY) / GameView.baseCellHeight;
				int j = (int) (e.getX() - GameView.this.offsetX) / GameView.baseCellWidth;
				
				GameView.this.cursor.i = i;
				GameView.this.cursor.j = j;
				
				if (!GameView.this.cursor.isVisible) GameView.this.cursor.isVisible = true;
				
				invalidate();
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
	
	public static void initResources(Resources res)
	{
		Unit.initResorces(res);
		
		// андроид говорит что так будет быстрее, чем HashMap<Integer, CellType>
		SparseArray<CellType> sparseArray = new SparseArray<CellType>();
		sparseArray.append(R.drawable.one_three, CellType.ONE_TREE);
		sparseArray.append(R.drawable.two_threes, CellType.TWO_TREES);
		sparseArray.append(R.drawable.three_trees, CellType.THREE_TREES);
		
		CellType cellType;
		int i = 0;
		while ((cellType = sparseArray.valueAt(i)) != null)
		{
			int id = sparseArray.keyAt(i);
			GameView.cells[i] = new Cell().setBitmap(res, id).setCellType(cellType);
			i++;
		}
	}
	
	protected void updateCells()
	{
		this.isOneBitmapLast = !this.isOneBitmapLast;
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
		final byte[][] field = this.client.getFieldMap();// .game.map.getField();
		for (int i = 0; i < field.length; i++)
			for (int j = 0; j < field[i].length; j++)
			{
				final Bitmap bitmap = GameView.cells[field[i][j]].bitmap;
				final int y = bitmap.getHeight() * i + this.offsetY;
				final int x = bitmap.getWidth() * j + this.offsetX;
				canvas.drawBitmap(bitmap, x, y, null);
			}
		
		// рисуем курсор (если есть)
		if (this.cursor.isVisible)
		{
			final int y = GameView.baseCellHeight * this.cursor.i - (Cursor.height - GameView.baseCellHeight) / 2 + this.offsetY;
			final int x = GameView.baseCellWidth * this.cursor.j - (Cursor.width - GameView.baseCellWidth) / 2 + this.offsetX;
			canvas.drawBitmap(this.isOneBitmapLast ? this.cursor.bitmupTwo : this.cursor.bitmupOne, x, y, null);
		}
		
	}
	
}
