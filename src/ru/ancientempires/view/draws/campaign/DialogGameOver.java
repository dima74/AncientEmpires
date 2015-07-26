package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.client.Client;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class DialogGameOver extends DialogFragment
{
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Поражение!");
		builder.setPositiveButton("Заново", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Client.getClient().restartGame();
				GameActivity.gameActivity.startGameView();
			}
		});
		builder.setNegativeButton("Выйти", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				getActivity().finish();
			}
		});
		return builder.create();
	}
	
}
