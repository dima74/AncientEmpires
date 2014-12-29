package ru.ancientempires.view;

import ru.ancientempires.action.ActionType;
import android.content.Context;
import android.view.View;

public abstract class GameViewPart extends View
{
	
	protected OldGameView	gameView;
	
	public GameViewPart(Context context, OldGameView gameView)
	{
		super(context);
		this.gameView = gameView;
	}
	
	public boolean update()
	{
		return false;
	}
	
	public boolean performAction(ActionType actionType)
	{
		return true;
	}
	
}
