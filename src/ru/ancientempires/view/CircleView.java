package ru.ancientempires.view;

import ru.ancientempires.framework.MyLog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.view.View;
import android.widget.FrameLayout;

public class CircleView extends FrameLayout
{
	
	public CircleView(Context context)
	{
		super(context);
		setWillNotDraw(false);
		setClipChildren(false);
		setRadius(0);
		
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
	}
	
	private View	view;
	
	private Path	path;
	public int		radius;
	public float	centerY;
	public float	centerX;
	
	public void setView(View view)
	{
		this.view = view;
		this.addView(view);
	}
	
	public void setRadius(int radius)
	{
		this.radius = radius;
		
		LayoutParams layoutParams = new LayoutParams(radius * 2, radius * 2);
		setLayoutParams(layoutParams);
		
		this.path = new Path();
		// this.path.addCircle(this.centerX, this.centerY, 100, Direction.CW);
		this.path.addRoundRect(new RectF(0, 0, 100, 100), 50, 50, Direction.CW);
		invalidate();
		if (this.view != null)
			this.view.invalidate();
	}
	
	public void setCenter(float centerY, float centerX)
	{
		this.centerY = centerY;
		this.centerX = centerX;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		// canvas.clipPath(this.path);
		super.dispatchDraw(canvas);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		MyLog.log(getHeight(), getWidth(), this.view.getHeight(), this.view.getWidth());
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.CYAN);
		// canvas.drawCircle(this.centerX, this.centerY, 300, paint);
		super.onDraw(canvas);
	}
	
}
