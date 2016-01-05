package ru.ancientempires.view.draws.campaign;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.activity.MainActivity;

public class DialogGameOver
{
	
	public void createDialog()
	{
		Builder builder = new Builder(GameActivity.activity);
		builder.setTitle("Поражение!");
		builder.setPositiveButton("Заново", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				GameActivity.startGame(GameActivity.activity, GameHandler.game.path.baseGameID, true);
			}
		});
		builder.setNegativeButton("В главное меню", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				GameActivity.activity.startActivity(new Intent(GameActivity.activity, MainActivity.class));
			}
		});
		builder.create();
	}
	
}
