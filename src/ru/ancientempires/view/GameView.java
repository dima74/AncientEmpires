package ru.ancientempires.view;

import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.model.UnitType;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	
	private GameViewThread	thread;
	
	private GestureDetector	detector;
	
	public GameActivity		gameActivity;
	
	public static int		h;
	public static int		w;
	
	public GameView(Context context)
	{
		super(context);
		getHolder().addCallback(this);
		this.detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDown(MotionEvent e)
			{
				return true;
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent event)
			{
				GameView.this.thread.touchY = event.getY();
				GameView.this.thread.touchX = event.getX();
				GameView.this.thread.interrupt();
				
				return true;
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
			{
				GameView.this.thread.gameDraw.offsetY -= distanceY;
				GameView.this.thread.gameDraw.offsetX -= distanceX;
				return true;
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return this.detector.onTouchEvent(event);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		this.thread = new GameViewThread(getHolder());
		this.thread.setRunning(true);
		this.thread.start();
		this.thread.gameDraw.onSizeChanged(GameView.w, GameView.h, 0, 0);
		this.thread.gameDraw.gameActivity = this.gameActivity;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		this.thread.setRunning(false);
		boolean retry = true;
		while (retry)
			try
			{
				this.thread.join();
				retry = false;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		// MyLog.l("GameView.surfaceDestroyed()");
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		GameView.h = h;
		GameView.w = w;
		if (this.thread != null)
			this.thread.gameDraw.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void performActionBuy(UnitType type)
	{
		this.thread.inputAlgoritmMain.performActionBuy(type);
	}
	
}
