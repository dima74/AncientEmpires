package ru.ancientempires.editor;

import ru.ancientempires.BaseThread;
import ru.ancientempires.BaseView;
import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.activities.EditorActivity;

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
