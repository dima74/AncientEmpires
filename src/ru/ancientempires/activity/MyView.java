package ru.ancientempires.activity;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ru.ancientempires.framework.MyLog;

public class MyView extends SurfaceView implements SurfaceHolder.Callback
{
	
	public MyView(Context context)
	{
		super(context);
		getHolder().addCallback(this);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		MyLog.l("surface Created");
		if (false)
			new Thread(new Runnable()
			{
				private long prev;
				
				@Override
				public void run()
				{
					while (true)
					{
						
						long time = System.currentTimeMillis();
						MyLog.l(time - prev);
						prev = time;
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		MyLog.l("surface Destroyed");
	}
	
}
