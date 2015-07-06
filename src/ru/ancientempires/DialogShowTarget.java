package ru.ancientempires;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.ScriptShowTarget;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DialogShowTarget extends DialogFragment
{
	
	private String				textTitle;
	private String				textTarget;
	private ScriptShowTarget	script;
	
	public DialogShowTarget(String textTitle, String textTarget, ScriptShowTarget script)
	{
		super();
		this.textTitle = textTitle;
		this.textTarget = textTarget;
		this.script = script;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(this.textTitle);
		builder.setMessage(this.textTarget);
		return builder.create();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		Campaign.finish(this.script);
	}
	
}
