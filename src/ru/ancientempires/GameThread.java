package ru.ancientempires;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.inputs.InputBase;
import ru.ancientempires.view.inputs.InputMain;

public class GameThread extends Thread
{
	
	public static final long	MILLISECONDS_BETWEEN_FRAMES	= 1000 / 30;
	private static GameThread	thread;
	
	private SurfaceHolder	surfaceHolder;
	public GameDrawMain		gameDraw;
	public InputMain		inputMain;
	
	volatile public boolean isRunning = true;
	
	public float	touchY;
	public float	touchX;
	public boolean	isTouch;
	
	volatile public boolean needUpdateCampaign = false;
	
	public GameThread(SurfaceHolder surfaceHolder)
	{
		Debug.create(this);
		this.surfaceHolder = surfaceHolder;
		
		gameDraw = InputBase.gameDraw = new GameDrawMain();
		gameDraw.inputMain = inputMain = new InputMain();
		gameDraw.inputPlayer = inputMain.inputPlayer;
	}
	
	@Override
	public void run()
	{
		Debug.onStart(this);
		if (GameThread.thread != null)
		{
			MyAssert.a(!GameThread.thread.isRunning);
			try
			{
				GameThread.thread.join();
			}
			catch (InterruptedException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
		}
		GameThread.thread = this;
		
		Client.getGame().campaign.start();
		
		while (isRunning)
		{
			long timeStart = System.currentTimeMillis();
			Canvas canvas = surfaceHolder.lockCanvas();
			if (canvas != null)
			{
				synchronized (surfaceHolder)
				{
					gameDraw.iFrame++;
					try
					{
						gameDraw.draw(canvas);
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
					gameDraw.touch(touchY, touchX);
				}
				catch (Exception e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				
			if (needUpdateCampaign)
			{
				needUpdateCampaign = false;
				Client.getGame().campaign.update();
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
		Debug.onStop(this);
	}
	
}
