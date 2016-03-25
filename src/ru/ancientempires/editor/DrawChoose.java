package ru.ancientempires.editor;

import android.graphics.Canvas;
import ru.ancientempires.draws.Draw;
import ru.ancientempires.draws.DrawAction;
import ru.ancientempires.draws.DrawInfo;
import ru.ancientempires.images.Images;

public class DrawChoose extends Draw
{
	
	public static float		mScale			= 2.0f;
	public static int		mA				= (int) (Images.get().bitmapSize * DrawInfo.mScale);
											
	public int				hBeforeBitmap	= mA / 4;
	public int				h				= mA + hBeforeBitmap * 2;
	public int				w				= super.w;
											
	public Callback			callback;
	public EditorStruct[]	structs;
	public int				selected		= 0;
	public DrawSelected		selectedDraw	= new DrawSelected(hBeforeBitmap - DrawSelected.h * 2);
											
	public void create(int n, Callback callback)
	{
		structs = new EditorStruct[n];
		this.callback = callback;
	}
	
	public void create(EditorStruct[] structs, Callback callback)
	{
		create(structs.length, callback);
		for (int i = 0; i < structs.length; i++)
			setStruct(i, structs[i]);
	}
	
	public void setStruct(int i, EditorStruct struct)
	{
		structs[i] = struct.createCopy();
		structs[i].y = (hBeforeBitmap + mA / 2) / mScale;
		structs[i].x = w * (i + 1) / (structs.length + 1) / mScale;
		if (i == selected)
		{
			updateSelectedDraw();
			selectedDraw.x = selectedDraw.targetX;
		}
	}
	
	private void updateSelectedDraw()
	{
		selectedDraw.targetX = (int) (structs[selected].x * mScale - mA / 2);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.save();
		canvas.drawRect(0, 0, w, h, DrawAction.whiteAlphaPaint);
		selectedDraw.draw(canvas);
		canvas.scale(mScale, mScale);
		for (EditorStruct struct : structs)
			struct.drawBitmap(canvas);
		canvas.restore();
	}
	
	public boolean touch(float touchY, float touchX)
	{
		if (touchY < 0 || touchX < 0 || touchY > h || touchX > w)
			return false;
		int nearestI = EditorStruct.getNearest(structs, touchY / mScale, touchX / mScale);
		if (selected == nearestI)
			callback.tapChoose(selected);
		selected = nearestI;
		updateSelectedDraw();
		return true;
	}
	
}
