package ru.ancientempires;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class TestView2 extends View
{
	
	public TestView2(Context context)
	{
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// super.onDraw(canvas);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.i);
		canvas.scale(2, 2);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}
	
}
