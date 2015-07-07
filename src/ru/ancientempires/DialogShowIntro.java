package ru.ancientempires;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.ScriptIntro;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.BitmapHelper;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogShowIntro extends DialogFragment
{
	
	private String		imagePath	= "campaigns/images/portraits/0.png";
	private String		text		= "Это длинный текст";
	private ScriptIntro	script;
	
	public DialogShowIntro(String imagePath, String text, ScriptIntro script)
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
			View view = getActivity().getLayoutInflater().inflate(R.layout.layout_intro, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
			TextView textView = (TextView) view.findViewById(R.id.textview);
			Bitmap bitmap = BitmapHelper.getBitmap(Client.gameZipFile, this.imagePath);
			imageView.setImageBitmap(bitmap);
			textView.setText(this.text);
			builder.setView(view);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return builder.create();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		Campaign.finish(this.script);
	}
	
}
