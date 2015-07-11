package ru.ancientempires;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.helpers.BitmapHelper;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDialogFragment extends DialogFragment
{
	
	private String			imagePath	= "campaigns/images/portraits/0.png";
	private String			text		= "Это длинный текст";
	private ScriptDialog	script;
	protected boolean		isClicked	= false;
	
	public MyDialogFragment(String imagePath, String text, ScriptDialog script)
	{
		this.imagePath = imagePath;
		this.text = text;
		this.script = script;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new Builder(getActivity());
		
		try
		{
			View view = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
			TextView textView = (TextView) view.findViewById(R.id.textview);
			Bitmap bitmap = BitmapHelper.getBitmap(Client.gameZipFile, this.imagePath);
			imageView.setImageBitmap(bitmap);
			textView.setText(this.text);
			builder.setView(view);
			
			Button button = (Button) view.findViewById(R.id.button);
			button.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
					// setUserVisibleHint(false);
					MyDialogFragment.this.isClicked = true;
				}
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return builder.create();
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
						e.printStackTrace();
					}
					Campaign.finish(MyDialogFragment.this.script);
				}
			}).run();
	}
	
}
