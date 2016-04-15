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
import ru.ancientempires.draws.campaign.DialogGameOver;
import ru.ancientempires.draws.campaign.DialogShowIntro;
import ru.ancientempires.draws.campaign.DialogShowTarget;
import ru.ancientempires.draws.campaign.MyDialog;
import ru.ancientempires.draws.campaign.MyDialogWithoutImage;

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
	public void gameOver(ScriptGameOver script)
	{
		new DialogGameOver().createDialog();
	}
	
	@Override
	public void vibrate()
	{
		GameActivity.vibrate();
	}
	
}
