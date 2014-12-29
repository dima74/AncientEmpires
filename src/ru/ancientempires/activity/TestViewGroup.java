package ru.ancientempires.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.widget.FrameLayout;

public class TestViewGroup extends FrameLayout
{
	
	Bitmap					source;
	Bitmap					newBitmap;
	private BitmapShader	shader;
	private Paint			paint;
	private float			radius;
	public static Canvas	canvas;
	
	public TestViewGroup(Context context)
	{
		super(context);
		final TestView child = new TestView(getContext());
		addView(child);
		setWillNotDraw(false);
		invalidate();
		
		if (false)
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					invalidate();
					child.invalidate();
				}
			}, 1000);
	}
	
	public static Paint getPaint(int color)
	{
		Paint paint = new Paint();
		paint.setColor(color);
		return paint;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		int a = 10;
		Paint green = TestViewGroup.getPaint(Color.GREEN);
		canvas.drawRect(a, a, getWidth() - a, getHeight() - a, green);
	}
	
}
