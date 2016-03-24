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
	public int				selectedBitmap	= 0;
	public DrawSelected		selectedDraw	= new DrawSelected(hBeforeBitmap - DrawSelected.h * 2);
											
	public void create(EditorStruct[] structs, Callback callback)
	{
		this.callback = callback;
		this.structs = structs;
		for (int i = 0; i < structs.length; i++)
		{
			structs[i].y = (hBeforeBitmap + mA / 2) / mScale;
			structs[i].x = w * (i + 1) / (structs.length + 1) / mScale;
		}
		updateSelectedDraw();
		selectedDraw.x = selectedDraw.targetX;
	}
	
	private void updateSelectedDraw()
	{
		selectedDraw.targetX = (int) (structs[selectedBitmap].x * mScale - mA / 2);
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
		if (selectedBitmap == nearestI)
			callback.tapChoose(selectedBitmap);
		selectedBitmap = nearestI;
		updateSelectedDraw();
		return true;
	}
	
}
