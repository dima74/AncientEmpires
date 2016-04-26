package ru.ancientempires;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ru.ancientempires.activity.BaseGameActivity;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;

public abstract class BaseView extends SurfaceView implements SurfaceHolder.Callback
{
	
	public BaseGameActivity	activity;
	public BaseThread		thread;
	public GestureDetector	detector;
							
	public int				h;
	public int				w;
							
	private boolean			isSurfaceCreated;
	private boolean			isSizeChanged;
							
	public BaseView(BaseGameActivity activity)
	{
		super(activity);
		this.activity = activity;
		Debug.onCreate(this);
		getHolder().addCallback(this);
		detector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener()
		{
			@Override
			public boolean onDown(MotionEvent e)
			{
				return thread.drawMain.isActiveGame();
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent event)
			{
				if (!thread.drawMain.isActiveGame())
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
				if (!thread.drawMain.isActiveGame())
					return false;
				thread.drawMain.onScroll(distanceY, distanceX);
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
		Debug.onStart(this);
		MyAssert.a(!isSurfaceCreated);
		isSurfaceCreated = true;
		onStart();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Debug.onStop(this);
		thread.isRunning = false;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		MyAssert.a(!isSizeChanged);
		this.h = h;
		this.w = w;
		isSizeChanged = true;
		onStart();
	}
	
	public abstract BaseThread createThread();
	
	public final void onStart()
	{
		if (isSizeChanged && isSurfaceCreated)
		{
			thread = createThread();
			thread.start();
		}
	}
	
}
