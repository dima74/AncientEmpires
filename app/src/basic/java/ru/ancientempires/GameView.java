package ru.ancientempires;

import android.view.SurfaceHolder;

import ru.ancientempires.activities.BaseGameActivity;

public class GameView extends BaseView implements SurfaceHolder.Callback
{
	
	public GameView(BaseGameActivity activity)
	{
		super(activity);
	}
	
	@Override
	public BaseThread createThread()
	{
		return new GameThread(activity, getHolder());
	}
	
}
