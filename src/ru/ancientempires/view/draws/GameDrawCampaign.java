package ru.ancientempires.view.draws;

import ru.ancientempires.DialogShowIntro;
import ru.ancientempires.DialogShowTarget;
import ru.ancientempires.IDrawCampaign;
import ru.ancientempires.MyDialogFragment;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.campaign.scripts.ScriptShowTarget;
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.campaign.scripts.ScriptTitle;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import android.app.DialogFragment;
import android.os.Handler;
import android.widget.Toast;

public class GameDrawCampaign extends GameDrawOnFramesGroup implements IDrawCampaign
{
	
	public GameDrawCampaign(GameDrawMain gameDraw)
	{
		super(gameDraw);
		Campaign.iDrawCampaign = this;
	}
	
	@Override
	public void showDialog(String imagePath, String text, ScriptDialog script)
	{
		DialogFragment dialogFragment = new MyDialogFragment(imagePath, text, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "MyDialogFragment");
	}
	
	@Override
	public void showTitle(final String text, final ScriptTitle script)
	{
		this.gameDraw.gameActivity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Toast.makeText(GameDrawCampaign.this.gameDraw.gameActivity, text, Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Campaign.finish(script);
					}
				}, 2000);
			}
		});
	}
	
	@Override
	public void showTarget(String textTitle, String textTarget, ScriptShowTarget script)
	{
		DialogFragment dialogFragment = new DialogShowTarget(textTitle, textTarget, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "DialogShowTarget");
	}
	
	@Override
	public void showIntro(String imagePath, String text, ScriptIntro script)
	{
		DialogFragment dialogFragment = new DialogShowIntro(imagePath, text, script);
		dialogFragment.show(this.gameDraw.gameActivity.getFragmentManager(), "DialogShowIntro");
	}
	
}
