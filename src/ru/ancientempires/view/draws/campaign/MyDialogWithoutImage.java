package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.ScriptDialogWithoutImage;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class MyDialogWithoutImage extends DialogFragment
{
	
	private String						text;
	private ScriptDialogWithoutImage	script;
	protected boolean					isClicked	= false;
	
	public MyDialogWithoutImage(String text, ScriptDialogWithoutImage script)
	{
		this.text = text;
		this.script = script;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new Builder(getActivity());
		builder.setMessage(this.text);
		builder.setPositiveButton("Далее", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dismiss();
				// setUserVisibleHint(false);
				MyDialogWithoutImage.this.isClicked = true;
			}
		});
		
		return builder.create();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
	}
	
	@Override
	public void onDetach()
	{
		MyLog.l(+System.currentTimeMillis() + " ondetach");
		super.onDetach();
		if (this.isClicked)
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						MyAssert.a(false);
						e.printStackTrace();
					}
					Campaign.finish(MyDialogWithoutImage.this.script);
				}
			}).start();
	}
	
}
