package ru.ancientempires.view;

import ru.ancientempires.view.algortihms.InputAlgoritmMain;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread
{
	
	public static final long	MILLISEC_BETWEEN_FRAMES	= 1000 / 30;
	
	private SurfaceHolder		surfaceHolder;
	public GameDrawMain			gameDraw;
	public InputAlgoritmMain	inputAlgoritmMain;
	
	volatile private boolean	isRunning;
	private long				startTime;
	private long				nextTime;
	
	volatile public float		touchY;
	volatile public float		touchX;
	
	volatile public boolean		isTap					= false;
	
	public GameViewThread(SurfaceHolder surfaceHolder)
	{
		this.surfaceHolder = surfaceHolder;
		
		this.gameDraw = new GameDrawMain();
		this.inputAlgoritmMain = new InputAlgoritmMain(this.gameDraw);
		this.gameDraw.inputAlgoritmMain = this.inputAlgoritmMain;
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
			if (timeToDraw <= 0)
			{
				this.nextTime += GameViewThread.MILLISEC_BETWEEN_FRAMES;
				canvas = this.surfaceHolder.lockCanvas();
				if (canvas != null)
				{
					synchronized (this.surfaceHolder)
					{
						this.gameDraw.draw(canvas);
						this.gameDraw.iFrame++;
					}
					this.surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			if (Thread.interrupted())
				touch();
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
		}
		// MyLog.l("GameViewThread.run() e");
	}
	
	private void touch()
	{
		if (this.gameDraw.touch(this.touchY, this.touchX))
		{
			final int i = (int) ((this.touchY - this.gameDraw.offsetY) / GameDraw.A);
			final int j = (int) ((this.touchX - this.gameDraw.offsetX) / GameDraw.A);
			this.inputAlgoritmMain.tap(i, j);
		}
	}
	
}
