package ru.ancientempires.draws.campaign;

import android.app.AlertDialog.Builder;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;

public class MyDialogWithoutImage extends MyDialogFragment
{
	
	public void showDialog(String text, ScriptDialogWithoutImage script)
	{
		Builder builder = new Builder(GameActivity.activity);
		builder.setMessage(text);
		showDialog(builder, script);
	}
	
}
