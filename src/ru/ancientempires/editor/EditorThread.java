package ru.ancientempires.editor;

import android.view.SurfaceHolder;
import ru.ancientempires.BaseThread;

public class EditorThread extends BaseThread
{
	
	public EditorInputMain inputMain;
	
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
	
}
