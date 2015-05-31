package ru.ancientempires.view;

import ru.ancientempires.view.algortihms.InputAlgorithmMain;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread
{
	
	public static final long	MILLISEC_BETWEEN_FRAMES	= 1000 / 30;
	
	private SurfaceHolder		surfaceHolder;
	public GameDrawMain			gameDraw;
	public InputAlgorithmMain	inputAlgorithmMain;
	
	volatile private boolean	isRunning;
	private long				startTime;
	private long				nextTime;
	
	volatile public float		touchY;
	volatile public float		touchX;
	
	volatile public boolean		isTap					= false;
	public boolean				isPause;
	
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
						try
						{
							this.gameDraw.draw(canvas);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						this.gameDraw.iFrame++;
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
					e.printStackTrace();
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
		if (!this.gameDraw.touch(this.touchY, this.touchX))
		{
			final int i = (int) ((this.touchY - this.gameDraw.offsetY) / GameDraw.A);
			final int j = (int) ((this.touchX - this.gameDraw.offsetX) / GameDraw.A);
			try
			{
				this.inputAlgorithmMain.tap(i, j);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
