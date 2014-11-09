package ru.ancientempires.view;

import ru.ancientempires.action.ActionType;
import android.content.Context;
import android.view.View;

public abstract class GameViewPart extends View
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
	
	public boolean performAction(ActionType actionType)
	{
		return true;
	}
	
}
