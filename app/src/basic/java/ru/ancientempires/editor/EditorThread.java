package ru.ancientempires.editor;

import android.view.SurfaceHolder;

import ru.ancientempires.BaseThread;
import ru.ancientempires.activities.EditorActivity;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GameSaver;

public class EditorThread extends BaseThread
{
	
	// public static EditorThread thread;
	
	public EditorInputMain  inputMain;
	public EditorChooseView view;

	public EditorThread(EditorActivity activity, SurfaceHolder surfaceHolder)
	{
		super(activity, surfaceHolder);
		
		drawMain = new EditorDrawMain();
		inputMain = new EditorInputMain(activity, (EditorDrawMain) drawMain);
		((EditorDrawMain) drawMain).setInputMain(inputMain);
		
		drawMain.cells.update();
		drawMain.cellsDual.update();
		drawMain.units.update();
		drawMain.buildingSmokes.update();
	}
	
	@Override
	public void beforeRun()
	{
		// thread = this;
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
		// thread = null;
		try
		{
			activity.game.trimPlayers();
			activity.game.currentPlayer = null;
			GameSaver.createBaseGame(activity.game);
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
}
