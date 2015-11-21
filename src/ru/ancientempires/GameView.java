package ru.ancientempires;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.view.draws.GameDraw;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	
	public GameThread thread;
	
	private GestureDetector detector;
	
	public static int	h;
	public static int	w;
	
	public GameView(Context context)
	{
		super(context);
		getHolder().addCallback(this);
		detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDown(MotionEvent e)
			{
				return thread.gameDraw.isActiveGame;
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent event)
			{
				thread.touchY = event.getY();
				thread.touchX = event.getX();
				thread.interrupt();
				return true;
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
			{
				thread.gameDraw.nextOffsetY -= distanceY / GameDraw.mapScale;
				thread.gameDraw.nextOffsetX -= distanceX / GameDraw.mapScale;
				return true;
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return detector.onTouchEvent(event);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		thread = new GameThread(getHolder());
		thread.setRunning(true);
		thread.start();
		
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
		thread.setRunning(false);
		boolean retry = true;
		while (retry)
			try
			{
				thread.join();
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
	}
	
}
