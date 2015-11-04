package ru.ancientempires.view;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.view.algortihms.InputAlgorithmMain;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameViewThread extends Thread
{
	
	public static final long MILLISEC_BETWEEN_FRAMES = 1000 / 30;
	
	private SurfaceHolder		surfaceHolder;
	public GameDrawMain			gameDraw;
	public InputAlgorithmMain	inputAlgorithmMain;
	
	volatile private boolean	isRunning;
	private long				startTime;
	private long				nextTime;
	
	volatile public float	touchY;
	volatile public float	touchX;
	
	volatile public boolean	isTap				= false;
	volatile public boolean	needUpdateCampaign	= false;
	public boolean			isPause;
	
	public GameViewThread(SurfaceHolder surfaceHolder)
	{
		this.surfaceHolder = surfaceHolder;
		
		this.gameDraw = new GameDrawMain();
		this.inputAlgorithmMain = new InputAlgorithmMain(this, this.gameDraw);
		this.gameDraw.inputAlgorithmMain = this.inputAlgorithmMain;
	}
	
	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
	
	@Override
	public void run()
	{
		this.startTime = this.nextTime = System.currentTimeMillis();
		this.gameDraw.startGame();
		
		Canvas canvas;
		while (this.isRunning)
		{
			long timeToDraw = this.nextTime - System.currentTimeMillis();
			
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
				this.nextTime += GameViewThread.MILLISEC_BETWEEN_FRAMES;
				canvas = this.surfaceHolder.lockCanvas();
				if (canvas != null)
				{
					synchronized (this.surfaceHolder)
					{
						this.gameDraw.iFrame++;
						try
						{
							this.gameDraw.draw(canvas);
						}
						catch (Exception e)
						{
							MyAssert.a(false);
							e.printStackTrace();
						}
					}
					this.surfaceHolder.unlockCanvasAndPost(canvas);
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
			else if (this.needUpdateCampaign)
				while (this.needUpdateCampaign)
				{
					this.needUpdateCampaign = false;
					Campaign.update();
				}
			timeToDraw = this.nextTime - System.currentTimeMillis() - 3;
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
				this.nextTime = System.currentTimeMillis();
		}
		// MyLog.l("GameViewThread.run() e");
	}
	
	private void touch()
	{
		this.gameDraw.touch(this.touchY, this.touchX);
	}
	
}
