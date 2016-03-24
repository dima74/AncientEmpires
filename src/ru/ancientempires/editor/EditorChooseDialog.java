package ru.ancientempires.editor;

import android.app.AlertDialog;
import ru.ancientempires.MyColor;
import ru.ancientempires.activity.EditorActivity;

public class EditorChooseDialog // extends DialogFragment
{
	
	public void show(final EditorStruct[] structs, final MyColor[] colors)
	{
		EditorActivity.activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.activity);
				AlertDialog dialog = builder.create();
				dialog.setView(new EditorChooseView(EditorActivity.activity, structs, colors));
				dialog.show();
			}
		});
	}
	
}
