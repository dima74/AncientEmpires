package ru.ancientempires;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.inputs.InputBase;
import ru.ancientempires.view.inputs.InputMain;

public class GameThread extends Thread
{
	
	public static final long MILLISEC_BETWEEN_FRAMES = 1000 / 30;
	
	private SurfaceHolder	surfaceHolder;
	public GameDrawMain		gameDraw;
	public InputMain		inputMain;
	
	volatile private boolean	isRunning;
	private long				startTime;
	private long				nextTime;
	
	volatile public float	touchY;
	volatile public float	touchX;
	
	volatile public boolean	isTap				= false;
	volatile public boolean	needUpdateCampaign	= false;
	public boolean			isPause;
	
	public GameThread(SurfaceHolder surfaceHolder)
	{
		this.surfaceHolder = surfaceHolder;
		
		InputBase.thread = this;
		GameDraw.game = InputBase.game = Client.getClient().getGame();
		gameDraw = InputBase.gameDraw = new GameDrawMain();
		gameDraw.inputMain = inputMain = new InputMain();
		gameDraw.inputPlayer = inputMain.inputPlayer;
	}
	
	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
	
	@Override
	public void run()
	{
		startTime = nextTime = System.currentTimeMillis();
		Campaign.start();
		
		Canvas canvas;
		while (isRunning)
		{
			long timeToDraw = nextTime - System.currentTimeMillis();
			
			/*
			while (this.isPause)
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{}
			*/
			
			if (timeToDraw <= 0)
			{
				nextTime += GameThread.MILLISEC_BETWEEN_FRAMES;
				canvas = surfaceHolder.lockCanvas();
				if (canvas != null)
				{
					synchronized (surfaceHolder)
					{
						GameDraw.iFrame++;
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
			}
			
			if (Thread.interrupted())
				try
				{
					touch();
				}
				catch (Exception e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			else if (needUpdateCampaign)
				while (needUpdateCampaign)
				{
					needUpdateCampaign = false;
					Campaign.update();
				}
			timeToDraw = nextTime - System.currentTimeMillis() - 3;
			if (timeToDraw > 0)
				try
				{
					Thread.sleep(timeToDraw);
				}
				catch (InterruptedException e)
				{
					touch();
				}
			else
				nextTime = System.currentTimeMillis();
		}
		// MyLog.l("GameViewThread.run() e");
	}
	
	private void touch()
	{
		gameDraw.touch(touchY, touchX);
	}
	
}
