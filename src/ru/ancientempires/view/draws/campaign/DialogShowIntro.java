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
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.images.CampaignImages;

public class DialogShowIntro extends DialogFragment
{
	
	private int			imageID;
	private String		text	= "Это длинный текст";
	private ScriptIntro	script;
	
	public DialogShowIntro(int imageID, String text, ScriptIntro script)
	{
		this.imageID = imageID;
		this.text = text;
		this.script = script;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new Builder(getActivity());
		
		View view = getActivity().getLayoutInflater().inflate(R.layout.layout_intro, null);
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
				Campaign.finish(script);
			}
		});
		
		return builder.create();
	}
	
}
