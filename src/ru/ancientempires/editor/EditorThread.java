package ru.ancientempires.editor;

import android.view.SurfaceHolder;
import ru.ancientempires.BaseThread;
import ru.ancientempires.activity.EditorActivity;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.save.GameSaver;

public class EditorThread extends BaseThread
{
	
	public static EditorThread	thread;
								
	public EditorInputMain		inputMain;
	public EditorChooseView		view;
								
	public EditorThread(SurfaceHolder surfaceHolder)
	{
		super(surfaceHolder);
		
		drawMain = new EditorDrawMain();
		inputMain = new EditorInputMain((EditorDrawMain) drawMain);
		((EditorDrawMain) drawMain).setInputMain(inputMain);
		
		drawMain.cells.update();
		drawMain.cellsDual.update();
		drawMain.units.update();
		drawMain.buildingSmokes.update();
	}
	
	@Override
	public void beforeRun()
	{
		thread = this;
	}
	
	@Override
	public void onRun()
	{
		if (view != null)
			view.postInvalidate();
	}
	
	@Override
	public void afterRun()
	{
		thread = null;
		try
		{
			new GameSaver(EditorActivity.activity.game).saveBaseGame();
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
}
