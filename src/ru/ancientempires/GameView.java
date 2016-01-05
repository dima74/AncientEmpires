package ru.ancientempires;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.view.draws.GameDraw;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	
	private GestureDetector	detector;
	public GameThread		thread;
	
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
				if (!thread.gameDraw.isActiveGame)
					return false;
				synchronized (thread)
				{
					thread.touchY = event.getY();
					thread.touchX = event.getX();
					thread.isTouch = true;
				}
				return true;
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
			{
				if (!thread.gameDraw.isActiveGame)
					return false;
				thread.gameDraw.onScroll(distanceY / GameDraw.mapScale, distanceX / GameDraw.mapScale);
				return true;
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// MyLog.l("GameView.onTouchEvent()");
		return detector.onTouchEvent(event);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		MyLog.l(hashCode() + " GameView.surfaceCreated()");
		thread = new GameThread(getHolder());
		thread.start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		MyLog.l(hashCode() + " GameView.surfaceDestroyed()");
		thread.isRunning = false;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		GameDraw.h = h;
		GameDraw.w = w;
	}
	
}
