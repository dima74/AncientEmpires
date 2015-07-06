package ru.ancientempires;

import java.io.IOException;

import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.scripts.ScriptDialog;
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

public class MyDialogFragment extends DialogFragment
{
	
	private String			bitmapPath	= "campaigns/images/portraits/0.png";
	private String			text		= "Это длинный текст";
	private ScriptDialog	script;
	
	public MyDialogFragment(String bitmapPath, String text, ScriptDialog script)
	{
		this.bitmapPath = bitmapPath;
		this.text = text;
		this.script = script;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Builder builder = new Builder(getActivity());
		
		try
		{
			View view = getActivity().getLayoutInflater().inflate(R.layout.layout, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
			TextView textView = (TextView) view.findViewById(R.id.textview);
			Bitmap bitmap = BitmapHelper.getBitmap(Client.gameZipFile, this.bitmapPath);
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
