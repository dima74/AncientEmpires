package ru.ancientempires;

import android.view.SurfaceHolder;

import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.draws.inputs.InputMain;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.PlayerType;

public class GameThread extends BaseThread
{
	
	// public static GameThread thread;
	
	public InputMain inputMain;
	volatile public boolean needUpdateCampaign = false;

	public GameThread(BaseGameActivity activity, SurfaceHolder surfaceHolder)
	{
		super(activity, surfaceHolder);
		// MyAssert.a(GameThread.thread == null);
		// GameThread.thread = this;
		
		drawMain = new DrawMain();
		inputMain = new InputMain((DrawMain) drawMain);
		((DrawMain) drawMain).setInputMain(inputMain);
	}
	
	@Override
	public void beforeRun()
	{
		GameActivity.activity.invalidateOptionsMenu();
		GameActivity.activity.game.campaign.start();
		if (drawMain.isActiveGame())
		{
			MyAssert.a(inputMain.game.currentPlayer.type == PlayerType.PLAYER);
			inputMain.beginTurn();
		}
	}
	
	@Override
	public void onRun()
	{
		if (needUpdateCampaign)
		{
			needUpdateCampaign = false;
			drawMain.game.campaign.update();
		}
	}
	
	@Override
	public void afterRun()
	{
		// thread = null;
		try
		{
			Client.client.stopGame();
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
}
