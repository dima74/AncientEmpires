package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import ru.ancientempires.BaseThread;
import ru.ancientempires.activity.BaseGameActivity;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.activity.MainActivity;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptDialogIntro;
import ru.ancientempires.campaign.scripts.ScriptDialogTarget;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;
import ru.ancientempires.campaign.scripts.ScriptDisableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptEnableActiveGame;
import ru.ancientempires.campaign.scripts.ScriptGameOver;
import ru.ancientempires.campaign.scripts.ScriptSnakeMap;
import ru.ancientempires.draws.campaign.DialogGameOver;
import ru.ancientempires.draws.campaign.DialogShowIntro;
import ru.ancientempires.draws.campaign.DialogShowTarget;
import ru.ancientempires.draws.campaign.MyDialog;
import ru.ancientempires.draws.campaign.MyDialogWithoutImage;
import ru.ancientempires.draws.onframes.DrawSnakeMap;

public class DrawCampaign extends BaseDrawCampaign
{
	
	@Override
	public void dialogIntro(Bitmap bitmap, String text, ScriptDialogIntro script)
	{
		new DialogShowIntro().showDialog(bitmap, text, script);
	}
	
	@Override
	public void dialog(Bitmap bitmap, String text, ScriptDialog script)
	{
		new MyDialog().showDialog(bitmap, text, script);
	}
	
	@Override
	public void dialog(String text, ScriptDialogWithoutImage script)
	{
		new MyDialogWithoutImage().showDialog(text, script);
	}
	
	@Override
	public void dialogTarget(String textTitle, String textTarget, ScriptDialogTarget script)
	{
		new DialogShowTarget().showDialog(textTitle, textTarget, script);
	}
	
	@Override
	public void toastTitle(final String text, final Script script)
	{
		BaseGameActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast toast = Toast.makeText(BaseGameActivity.activity, text, Toast.LENGTH_SHORT);
				TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
				if (v != null)
					v.setGravity(Gravity.CENTER);
				toast.show();
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
	
	//
	@Override
	public void enableActiveGame(ScriptEnableActiveGame script)
	{
		super.enableActiveGame(script);
		BaseGameActivity.activity.invalidateOptionsMenu();
	}
	
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script)
	{
		main.isActiveGame = false;
		BaseGameActivity.activity.invalidateOptionsMenu();
	}

	@Override
	public void closeMission() throws Exception
	{
		super.closeMission();
		if (game.path.nextGameID == null)
			GameActivity.activity.moveTo(MainActivity.class);
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
	public void snakeMap(ScriptSnakeMap script)
	{
		int frames = script.milliseconds / BaseThread.MILLISECONDS_BETWEEN_FRAMES;
		add(new DrawSnakeMap().animate(frames), script);
	}
	
}
