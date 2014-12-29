package ru.ancientempires.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout.LayoutParams;

public class ZoneView extends View
{
	
	protected OldGameView	gameView;
	
	public ZoneView(Context context, OldGameView gameView)
	{
		super(context);
		this.gameView = gameView;
		setZone(new boolean[0][0]);
		
		this.circlePaint = new Paint();
		this.circlePaint.setColor(Color.TRANSPARENT);
		this.circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		this.circlePaint.setStyle(Paint.Style.STROKE);
		
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
	}
	
	private final Paint	circlePaint;
	
	private Bitmap		bitmap;
	
	public ZoneView setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
		return this;
	}
	
	private int			amount;
	private boolean[][]	zone;
	
	private int			radiusStart;
	private int			radiusEnd;
	
	public boolean		isInverse	= false;
	
	public ZoneView setRadius(int amount)
	{
		this.amount = amount;
		
		this.radiusStart = OldGameView.baseH / 2;
		this.radiusEnd = (int) ((ZoneView.this.amount + 0.5f) * OldGameView.baseH);
		
		LayoutParams layoutParams = new LayoutParams(this.radiusEnd * 2, this.radiusEnd * 2);
		setLayoutParams(layoutParams);
		
		return this;
	}
	
	public ZoneView setZone(boolean[][] zone)
	{
		this.zone = zone;
		return this;
	}
	
	public ZoneView startAnimate()
	{
		ValueAnimator animator = ValueAnimator.ofInt((this.radiusEnd - this.radiusStart) * 2, 0);
		animator.setDuration(this.amount * 1000 / 7);
		if (this.isInverse)
		{
			animator = ValueAnimator.ofInt(0, (this.radiusEnd - this.radiusStart) * 2);
			animator.setDuration(this.amount * 1000 / 20);
		}
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				ZoneView.this.circlePaint.setStrokeWidth((int) animation.getAnimatedValue());
				postInvalidate();
			}
		});
		animator.start();
		return this;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		for (int i = 0; i < this.zone.length; i++)
			for (int j = 0; j < this.zone[i].length; j++)
				if (this.zone[i][j])
					canvas.drawBitmap(this.bitmap, j * OldGameView.baseW, i * OldGameView.baseH, null);
		
		canvas.translate(this.radiusEnd, this.radiusEnd);
		canvas.drawCircle(0, 0, this.radiusEnd, this.circlePaint);
	}
	
}
