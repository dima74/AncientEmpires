package ru.ancientempires.draws.campaign;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.activities.MainActivity;

public class DialogGameOver
{
	
	public void createDialog()
	{
		final Builder builder = new Builder(GameActivity.activity);
		builder.setCancelable(false);
		builder.setTitle("Поражение!");
		builder.setPositiveButton("Заново", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				GameActivity.activity.restartGame();
			}
		});
		builder.setNegativeButton("В главное меню", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				GameActivity.activity.moveTo(MainActivity.class);
			}
		});
		GameActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				builder.show();
			}
		});
	}
	
}
