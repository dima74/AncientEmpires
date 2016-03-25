package ru.ancientempires.editor;

import android.content.Context;
import android.view.SurfaceHolder;
import ru.ancientempires.BaseView;
import ru.ancientempires.activity.EditorActivity;

public class EditorView extends BaseView
{
	
	public EditorView(Context context)
	{
		super(context);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		super.surfaceCreated(holder);
		thread = EditorActivity.activity.thread = new EditorThread(getHolder());
		thread.start();
	}
	
}
