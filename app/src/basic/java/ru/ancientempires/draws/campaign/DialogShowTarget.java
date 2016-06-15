package ru.ancientempires.draws.campaign;

import android.app.AlertDialog;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.campaign.scripts.ScriptDialogTarget;

public class DialogShowTarget extends MyDialogFragment
{
	
	public void showDialog(String textTitle, String textTarget, ScriptDialogTarget script)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.activity);
		builder.setTitle(textTitle);
		builder.setMessage(textTarget);
		showDialog(builder, script);
	}
	
}
