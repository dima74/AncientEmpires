package ru.ancientempires.editor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.BaseView;
import ru.ancientempires.draws.Draw;
import ru.ancientempires.draws.DrawAction;
import ru.ancientempires.draws.DrawInfo;
import ru.ancientempires.images.Images;

public class DrawChoose extends Draw
{
	
	public static float	mScale			= 2.0f;
	public static int	mA				= (int) (Images.get().bitmapSize * DrawInfo.mScale);
										
	public int			hBeforeBitmap	= mA / 4;
	public int			h				= DrawInfo.mA + hBeforeBitmap * 2;
	public int			w				= super.w;
										
	public int			numberBitmaps	= 3;
	public Bitmap[]		bitmaps			= new Bitmap[numberBitmaps];
										
	public int			selectedBitmap	= 0;
	public DrawSelected	selectedDraw	= new DrawSelected(this);
										
	public DrawChoose()
	{}
	
	public void setBitmap(int i, Bitmap bitmap)
	{
		int newH = (int) (bitmap.getHeight() * mScale);
		int newW = (int) (bitmap.getWidth() * mScale);
		bitmaps[i] = Bitmap.createScaledBitmap(bitmap, newH, newW, false);
		if (i == selectedBitmap)
			selectedDraw.x = (int) getCurrentBitmapLeftX();
	}
	
	private float getBitmapCenterY(int i)
	{
		return hBeforeBitmap + bitmaps[i].getHeight() / 2;
	}
	
	public float getBitmapCenterX(int i)
	{
		return w / (float) (numberBitmaps + 1) * (i + 1);
	}
	
	public float getCurrentBitmapLeftX()
	{
		return getBitmapCenterX(selectedBitmap) - bitmaps[selectedBitmap].getWidth() / 2;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.save();
		canvas.translate(0, BaseView.h - h);
		canvas.drawRect(0, 0, w, h, DrawAction.whiteAlphaPaint);
		for (int i = 0; i < numberBitmaps; i++)
		{
			Bitmap bitmap = bitmaps[i];
			float y = getBitmapCenterY(i) - bitmap.getHeight() / 2;
			float x = getBitmapCenterX(i) - bitmap.getWidth() / 2;
			canvas.drawBitmap(bitmap, x, y, null);
		}
		selectedDraw.draw(canvas);
		canvas.restore();
	}
	
	public boolean touch(float touchY, float touchX)
	{
		if (touchY < 0 || touchX < 0 || touchY > h || touchX > w)
			return false;
		int nearestI = getNearestI(touchY, touchX);
		if (selectedBitmap == nearestI)
			EditorDrawMain.main.inputMain.choose(selectedBitmap);
		selectedBitmap = nearestI;
		return true;
	}
	
	private int getNearestI(float touchY, float touchX)
	{
		int nearest = 0;
		for (int i = 1; i < numberBitmaps; i++)
			if (getDistance(i, touchY, touchX) < getDistance(nearest, touchY, touchX))
				nearest = i;
		return nearest;
	}
	
	private double getDistance(int i, float touchY, float touchX)
	{
		return Math.hypot(getBitmapCenterY(i) - touchY, getBitmapCenterX(i) - touchX);
	}
	
}
