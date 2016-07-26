package ru.ancientempires.draws.campaign;

import android.app.AlertDialog.Builder;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;

public class MyDialogWithoutImage extends MyDialogFragment
{
	
	public void showDialog(GameActivity activity, String text, ScriptDialogWithoutImage script)
	{
		Builder builder = new Builder(activity);
		builder.setMessage(text);
		showDialog(activity, builder, script);
	}
	
}
