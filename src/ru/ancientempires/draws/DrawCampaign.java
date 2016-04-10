package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.Toast;
import ru.ancientempires.activity.BaseGameActivity;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;
import ru.ancientempires.campaign.scripts.ScriptDisableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptEnableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptGameOver;
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.campaign.scripts.ScriptShowTarget;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.campaign.DialogGameOver;
import ru.ancientempires.draws.campaign.DialogShowIntro;
import ru.ancientempires.draws.campaign.DialogShowTarget;
import ru.ancientempires.draws.campaign.DrawCameraMove;
import ru.ancientempires.draws.campaign.MyDialog;
import ru.ancientempires.draws.campaign.MyDialogWithoutImage;
import ru.ancientempires.draws.onframes.DrawUnitMove;

public class DrawCampaign extends BaseDrawCampaign
{
	
	@Override
	public void showIntro(Bitmap bitmap, String text, ScriptIntro script)
	{
		new DialogShowIntro().showDialog(bitmap, text, script);
	}
	
	@Override
	public void showTitle(final String text, final Script script)
	{
		BaseGameActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(BaseGameActivity.activity, text, Toast.LENGTH_SHORT)
						.show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						script.finish();
					}
				}, 2000);
			}
		});
	}
	
	@Override
	public void showDialog(Bitmap bitmap, String text, ScriptDialog script)
	{
		new MyDialog().showDialog(bitmap, text, script);
	}
	
	@Override
	public void showDialog(String text, ScriptDialogWithoutImage script)
	{
		new MyDialogWithoutImage().showDialog(text, script);
	}
	
	@Override
	public void showTarget(String textTitle, String textTarget, ScriptShowTarget script)
	{
		new DialogShowTarget().showDialog(textTitle, textTarget, script);
	}
	
	//
	@Override
	public void enableActiveGame(ScriptEnableActiveGame script)
	{
		main.isActiveGame = true;
		main.isDrawCursor = false;
		DrawUnitMove.framesForCell = 8;
		DrawCameraMove.delta = 6;
		if (main.infoY != 0)
			main.infoMove.startShow();
		BaseGameActivity.activity.invalidateOptionsMenu();
		script.performAction();
	}
	
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script)
	{
		main.isActiveGame = false;
		BaseGameActivity.activity.invalidateOptionsMenu();
	}
	
	@Override
	public void gameOver(ScriptGameOver script)
	{
		new DialogGameOver().createDialog();
	}
	
	@Override
	public void vibrate()
	{
		GameActivity.vibrate();
	}
	
	@Override
	public void closeMission() throws Exception
	{
		Client.client.stopGame();
		if (game.path.nextGameID != null)
			GameActivity.startGame(game.path.nextGameID, false);
	}
	
}
