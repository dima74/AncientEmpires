package ru.ancientempires;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.draws.inputs.InputMain;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;

public class GameThread extends Thread
{
	
	public static final long	MILLISECONDS_BETWEEN_FRAMES	= 1000 / 30;
	public static GameThread	thread;
								
	private SurfaceHolder		surfaceHolder;
	public DrawMain				drawMain;
	public InputMain			inputMain;
								
	volatile public boolean		isRunning					= true;
															
	public float				touchY;
	public float				touchX;
	public boolean				isTouch;
								
	volatile public boolean		needUpdateCampaign			= false;
															
	public GameThread(SurfaceHolder surfaceHolder)
	{
		Debug.create(this);
		this.surfaceHolder = surfaceHolder;
		
		MyAssert.a(GameThread.thread == null || !GameThread.thread.isAlive());
		GameThread.thread = this;
		
		drawMain = new DrawMain();
		inputMain = new InputMain(drawMain);
		drawMain.setInputMain(inputMain);
	}
	
	private Runnable runnable;
	
	public void runOnGameThread(Runnable runnable)
	{
		MyAssert.a(this.runnable == null);
		this.runnable = runnable;
	}
	
	@Override
	public void run()
	{
		Debug.onStart(this);
		GameActivity.activity.invalidateOptionsMenu();
		GameActivity.activity.game.campaign.start();
		
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
			if (isTouch)
				try
				{
					drawMain.touch(touchY, touchX);
				}
				catch (Exception e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			if (needUpdateCampaign)
			{
				needUpdateCampaign = false;
				drawMain.game.campaign.update();
			}
			if (runnable != null)
				synchronized (this)
				{
					runnable.run();
					runnable = null;
				}
				
			// TODO сделать другой тайминговый механизм: не засыпать тут, а передавать в gameDraw.draw() время, прошедшее с последнего рисования. По нему впринципе можно вычислить iFrame, если не хочется переписывать все draw()
			long timeElapsed = System.currentTimeMillis() - timeStart;
			long timeToSleep = GameThread.MILLISECONDS_BETWEEN_FRAMES - timeElapsed;
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
		
		try
		{
			Client.client.stopGame();
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		
		Debug.onStop(this);
	}
	
}
