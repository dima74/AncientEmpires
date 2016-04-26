package ru.ancientempires.editor;

import ru.ancientempires.BaseThread;
import ru.ancientempires.BaseView;
import ru.ancientempires.activity.BaseGameActivity;
import ru.ancientempires.activity.EditorActivity;

public class EditorView extends BaseView
{
	
	public EditorView(BaseGameActivity activity)
	{
		super(activity);
	}
	
	@Override
	public BaseThread createThread()
	{
		return new EditorThread((EditorActivity) activity, getHolder());
	}
	
}
