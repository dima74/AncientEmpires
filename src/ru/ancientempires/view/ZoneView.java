package ru.ancientempires.view;

import ru.ancientempires.model.Point;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

public class ZoneView extends View
{
	
	public ZoneView(Context context)
	{
		super(context);
		setWillNotDraw(false);
		// setLayerType(View.LAYER_TYPE_HARDWARE, null);
	}
	
	private Bitmap		bitmap;
	private int			n;
	private float		r;
	private boolean[][]	zone;
	
	public ZoneView setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
		return this;
	}
	
	public ZoneView setRadius(int n)
	{
		this.n = n;
		this.zone = new boolean[2 * n + 1][2 * n + 1];
		this.r = (n + 0.5f) * GameView.baseH;
		return this;
	}
	
	public ZoneView setZone(Point[] points)
	{
		for (Point point : points)
			this.zone[this.n + point.i][this.n + point.j] = true;
		return this;
	}
	
	Paint	clearPaint;
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		for (int i = 0; i < this.zone.length; i++)
			for (int j = 0; j < this.zone[i].length; j++)
				if (this.zone[i][j])
					canvas.drawBitmap(this.bitmap, j * GameView.baseW, i * GameView.baseH, null);
		
		// canvas.translate(this.r, this.r);
		
		Paint ovalPaint = new Paint();
		// ovalPaint.setColor(Color.TRANSPARENT);
		ovalPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		ovalPaint.setStyle(Paint.Style.STROKE);
		ovalPaint.setStrokeWidth(this.r / 2);
		canvas.drawOval(new RectF(this.r * 0.5f, this.r * 0.5f, this.r * 1.5f, this.r * 1.5f), ovalPaint);
	}
}
