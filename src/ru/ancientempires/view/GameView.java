package ru.ancientempires.view;

import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.framework.MyAssert;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	
	public GameViewThread	thread;
	
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
				return GameView.this.thread.gameDraw.isActiveGame;
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
				GameView.this.thread.gameDraw.nextOffsetY -= distanceY;
				GameView.this.thread.gameDraw.nextOffsetX -= distanceX;
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
		this.thread.gameDraw.onSizeChanged(GameView.w, GameView.h, 0, 0);
		this.thread.gameDraw.gameActivity = this.gameActivity;
		this.thread.setRunning(true);
		this.thread.start();
		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		stopThread();
		// MyLog.l("GameView.surfaceDestroyed()");
	}
	
	public void stopThread()
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
				MyAssert.a(false);
				e.printStackTrace();
			}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		GameView.h = h;
		GameView.w = w;
		if (this.thread != null)
			this.thread.gameDraw.onSizeChanged(w, h, oldw, oldh);
	}
	
}
