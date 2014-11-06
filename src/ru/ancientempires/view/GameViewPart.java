package ru.ancientempires.view;

import android.content.Context;
import android.widget.FrameLayout;

public abstract class GameViewPart extends FrameLayout
{
	
	protected GameView	gameView;
	
	public GameViewPart(Context context, GameView gameView)
	{
		super(context);
		setWillNotDraw(false);
		this.gameView = gameView;
	}
	
	public boolean update()
	{
		return false;
	}
	
	public void updateOffset()
	{
		
	}
	
}
