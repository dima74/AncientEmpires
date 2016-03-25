package ru.ancientempires.editor;

import android.graphics.Canvas;
import ru.ancientempires.BaseDrawMain;

public class EditorDrawMain extends BaseDrawMain
{
	
	public static EditorDrawMain main;
	
	@Override
	public void setupStatic()
	{
		main = this;
	}
	
	public EditorInputMain inputMain;
	
	public void setInputMain(EditorInputMain inputMain)
	{
		this.inputMain = inputMain;
	}
	
	public DrawChoose choose = new DrawChoose();
	
	@Override
	public void setVisibleMapSize()
	{
		visibleMapH = h - choose.h;
		visibleMapW = w;
	}
	
	public EditorDrawMain()
	{
		initOffset();
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		canvas.restore();
		
		canvas.translate(0, h - choose.h);
		choose.draw(canvas);
	}
	
	@Override
	public void touch(float touchY, float touchX)
	{
		if (choose.touch(touchY - (h - choose.h), touchX))
			return;
		super.touch(touchY, touchX);
	}
	
	@Override
	public void tap(int i, int j)
	{
		inputMain.tapMap(i, j);
	}
	
}
