package ru.ancientempires.draws.campaign;

import android.app.AlertDialog;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.campaign.scripts.ScriptDialogTarget;

public class DialogShowTarget extends MyDialogFragment {

	public void showDialog(GameActivity activity, String textTitle, String textTarget, ScriptDialogTarget script) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(textTitle);
		builder.setMessage(textTarget);
		showDialog(activity, builder, script);
	}

}
