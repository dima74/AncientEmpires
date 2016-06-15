package ru.ancientempires.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.ancientempires.R;

public class TestActivity extends Activity
{
	
	class TestView extends View
	{
		Bitmap bitmap;
		
		TestView(Context context)
		{
			super(context);
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.i);
		}
		
		long measure(Canvas canvas, int y, int x)
		{
			long s = System.nanoTime();
			canvas.drawBitmap(bitmap, x, y, null);
			long e = System.nanoTime();
			return e - s;
		}
		
		@Override
		protected void onDraw(final Canvas canvas)
		{
			Rect bounds = canvas.getClipBounds();
			Log.e("ae", bounds.toString());
			
			long t2 = measure(canvas, 1000, 1000);
			long t1 = measure(canvas, 0, 0);
			Log.e("ae", String.format("%f %f\n", t1 / 1e3, t2 / 1e3));
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final TestView view = new TestView(this);
		setContentView(view);
		new Thread()
		{
			@Override
			public void run()
			{
				while (true)
					// view.postInvalidate();
					try
					{
						Thread.sleep(500);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}.start();
	}
	
}
