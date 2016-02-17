package ru.ancientempires.draws.campaign;

import android.app.AlertDialog;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.campaign.scripts.ScriptShowTarget;

public class DialogShowTarget extends MyDialogFragment
{
	
	public void showDialog(String textTitle, String textTarget, ScriptShowTarget script)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.activity);
		builder.setTitle(textTitle);
		builder.setMessage(textTarget);
		showDialog(builder, script);
	}
	
}
