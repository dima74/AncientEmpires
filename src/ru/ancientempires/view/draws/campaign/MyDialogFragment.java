package ru.ancientempires.view.draws.campaign;

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
import ru.ancientempires.R;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.ScriptDialog;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.CampaignImages;

public class MyDialogFragment extends DialogFragment
{
	
	private int				imageID;
	private String			text		= "Это длинный текст";
	private ScriptDialog	script;
	protected boolean		isClicked	= false;
	
	public MyDialogFragment(int imagePath, String text, ScriptDialog script)
	{
		imageID = imagePath;
		this.text = text;
		this.script = script;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new Builder(getActivity());
		
		View view = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageUnit);
		TextView textView = (TextView) view.findViewById(R.id.textUnitName);
		
		CampaignImages<Bitmap> images = CampaignImages.images;
		Bitmap bitmap = images.getImage(imageID);
		
		imageView.setImageBitmap(bitmap);
		textView.setText(text);
		builder.setView(view);
		
		Button button = (Button) view.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dismiss();
				// setUserVisibleHint(false);
				isClicked = true;
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
		if (isClicked)
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
					Campaign.finish(script);
				}
			}).start();
	}
	
}
