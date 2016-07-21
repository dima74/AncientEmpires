package ru.ancientempires;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;

public class BaseThread extends Thread
{
	
	public static final int MILLISECONDS_BETWEEN_FRAMES = 1000 / 30;

	public BaseGameActivity activity;
	public SurfaceHolder    surfaceHolder;
	public BaseDrawMain     drawMain;

	volatile public boolean isRunning = true;

	public float   touchY;
	public float   touchX;
	public boolean isTouch;

	public BaseThread(BaseGameActivity activity, SurfaceHolder surfaceHolder)
	{
		Debug.onCreate(this);
		setName("Game Thread");
		this.surfaceHolder = surfaceHolder;
		this.activity = activity;
	}
	
	public Runnable runnable;
	
	public void runOnGameThread(Runnable runnable)
	{
		MyAssert.a(this.runnable == null);
		MyAssert.a(runnable != null);
		this.runnable = runnable;
	}
	
	public void beforeRun()
	{}
	
	@Override
	public void run()
	{
		Debug.onStart(this);
		beforeRun();
		while (isRunning)
		{
			long timeStart = System.currentTimeMillis();
			Canvas canvas = surfaceHolder.lockCanvas();
			if (canvas != null)
			{
				synchronized (surfaceHolder)
				{
					drawMain.iFrame++;
					try
					{
						drawMain.draw(canvas);
					}
					catch (Exception e)
					{
						MyAssert.a(false);
						e.printStackTrace();
					}
				}
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
			
			boolean isTouch = false;
			float touchY = 0, touchX = 0;
			synchronized (this)
			{
				if (this.isTouch)
				{
					this.isTouch = false;
					isTouch = true;
					touchY = this.touchY;
					touchX = this.touchX;
				}
			}
			try
			{
				if (isTouch)
					drawMain.touch(touchY, touchX);
				if (runnable != null)
				{
					runnable.run();
					runnable = null;
				}
			}
			catch (Exception e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
			onRun();
			
			// TODO сделать другой тайминговый механизм: не засыпать тут, а передавать в gameDraw.draw() время, прошедшее с последнего рисования. По нему впринципе можно вычислить iFrame, если не хочется переписывать все draw()
			long timeElapsed = System.currentTimeMillis() - timeStart;
			long timeToSleep = MILLISECONDS_BETWEEN_FRAMES - timeElapsed;
			if (timeToSleep > 0)
				try
				{
					Thread.sleep(timeToSleep);
				}
				catch (InterruptedException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
		}
		afterRun();
		Debug.onStop(this);
	}
	
	public void onRun()
	{}
	
	public void afterRun()
	{}
	
}
