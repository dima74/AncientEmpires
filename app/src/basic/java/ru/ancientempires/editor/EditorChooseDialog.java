package ru.ancientempires.editor;

import android.app.AlertDialog;
import ru.ancientempires.MyColor;
import ru.ancientempires.activity.EditorActivity;

public class EditorChooseDialog
{
	
	public void show(final EditorActivity activity, final EditorStruct[] structs, final MyColor[] colors, final int color)
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setView(new EditorChooseView(builder.getContext(), activity, structs, colors, color));
				activity.dialog = builder.show();
			}
		});
	}
	
}
