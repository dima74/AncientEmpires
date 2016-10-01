package ru.ancientempires.draws.campaign;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.activities.MainActivity;

public class DialogGameOver {

	public static void createDialog(final GameActivity activity) {
		final Builder builder = new Builder(activity);
		builder.setCancelable(false);
		builder.setTitle("Поражение!");
		builder.setPositiveButton("Заново", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.restartGame();
			}
		});
		builder.setNegativeButton("В главное меню", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.moveTo(MainActivity.class);
			}
		});
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				builder.show();
			}
		});
	}
}
