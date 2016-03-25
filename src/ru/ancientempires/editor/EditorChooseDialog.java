package ru.ancientempires.editor;

import android.app.AlertDialog;
import android.app.Dialog;
import ru.ancientempires.MyColor;
import ru.ancientempires.activity.EditorActivity;

public class EditorChooseDialog
{
	
	public static Dialog dialog;
	
	public void show(final EditorStruct[] structs, final MyColor[] colors, final int color)
	{
		EditorActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.activity);
				builder.setView(new EditorChooseView(builder.getContext(), structs, colors, color));
				dialog = builder.show();
			}
		});
	}
	
}
