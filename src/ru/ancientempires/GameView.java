package ru.ancientempires;

import android.content.Context;
import android.view.SurfaceHolder;

public class GameView extends BaseView implements SurfaceHolder.Callback
{
	
	public GameView(Context context)
	{
		super(context);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		super.surfaceCreated(holder);
		thread = new GameThread(getHolder());
		thread.start();
	}
	
}
