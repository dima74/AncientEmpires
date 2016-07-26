package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import ru.ancientempires.BaseThread;
import ru.ancientempires.activities.MainActivity;
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

	public DrawCampaign(BaseDrawMain mainBase)
	{
		super(mainBase);
	}

	@Override
	public void dialogIntro(Bitmap bitmap, String text, ScriptDialogIntro script)
	{
		new DialogShowIntro().showDialog(getGameActivity(), bitmap, text, script);
	}
	
	@Override
	public void dialog(Bitmap bitmap, String text, ScriptDialog script)
	{
		new MyDialog().showDialog(getGameActivity(), bitmap, text, script);
	}
	
	@Override
	public void dialog(String text, ScriptDialogWithoutImage script)
	{
		new MyDialogWithoutImage().showDialog(getGameActivity(), text, script);
	}
	
	@Override
	public void dialogTarget(String textTitle, String textTarget, ScriptDialogTarget script)
	{
		new DialogShowTarget().showDialog(getGameActivity(), textTitle, textTarget, script);
	}
	
	@Override
	public void toastTitle(final String text, final Script script)
	{
		main.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast toast = Toast.makeText(main.activity, text, Toast.LENGTH_SHORT);
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
		main.activity.invalidateOptionsMenu();
	}
	
	@Override
	public void disableActiveGame(ScriptDisableActiveGame script)
	{
		main.isActiveGame = false;
		main.activity.invalidateOptionsMenu();
		script.performAction();
	}

	@Override
	public void closeMission() throws Exception
	{
		super.closeMission();
		if (game.path.nextGameID == null)
			main.activity.moveTo(MainActivity.class);
	}

	@Override
	public void gameOver(ScriptGameOver script)
	{
		DialogGameOver.createDialog(getGameActivity());
	}

	@Override
	public void vibrate()
	{
		main.activity.vibrate();
	}

	@Override
	public void snakeMap(ScriptSnakeMap script)
	{
		int frames = script.milliseconds / BaseThread.MILLISECONDS_BETWEEN_FRAMES;
		add(new DrawSnakeMap(mainBase).animate(frames), script);
	}
	
}
