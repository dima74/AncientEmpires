package ru.ancientempires.view;

import ru.ancientempires.images.BigNumberImages;
import ru.ancientempires.images.SparksImages;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimateDecreaseHealthView extends View
{
	
	private static final int		TIME_ANIMATION_HEALTH	= 1548;	// DELAY * 5.84
	private static final int		TIME_ANIMATION_SPARKS	= 768;		// HEALTH / 2
																		
	private static final float[]	ys						= new float[]
															{
			9, 9, 9, 9, 6, 6, 6, 6, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3,
			3, 4, 4, 4, 4, 6, 6, 6, 9, 9, 9, 9, 13, 13, 13, 13, 11,
			11, 11, 11, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11,
			11, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12, 13,
			13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
			13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13
															};
	
	private float					y;
	
	private int						yStart;
	private int						xStart;
	private int						health;
	
	private long					startSparkAnimateTime;
	private boolean					isSparkAnimate			= false;
	
	public AnimateDecreaseHealthView(Context context)
	{
		super(context);
		setLayoutParams(GameView.fullLayoutParams);
		setVisibility(View.GONE);
	}
	
	public void startAnimate(int yStart, int xStart, int health)
	{
		this.yStart = yStart;
		this.xStart = xStart;
		this.health = health;
		
		setVisibility(View.VISIBLE);
		ValueAnimator animator = ValueAnimator.ofFloat(AnimateDecreaseHealthView.ys);
		animator.setDuration(AnimateDecreaseHealthView.TIME_ANIMATION_HEALTH);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				AnimateDecreaseHealthView.this.y = (float) animation.getAnimatedValue() * GameView.a;
				invalidate();
			}
		});
		animator.addListener(new AnimatorListener()
		{
			@Override
			public void onAnimationStart(Animator animation)
			{}
			
			@Override
			public void onAnimationRepeat(Animator animation)
			{}
			
			@Override
			public void onAnimationEnd(Animator animation)
			{
				setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationCancel(Animator animation)
			{}
		});
		animator.start();
		
		this.startSparkAnimateTime = System.currentTimeMillis();
		this.isSparkAnimate = true;
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				AnimateDecreaseHealthView.this.isSparkAnimate = false;
			}
		}, AnimateDecreaseHealthView.TIME_ANIMATION_SPARKS);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		final long millis = System.currentTimeMillis();
		canvas.translate(this.xStart, this.yStart);
		drawDecreaseHealth(canvas, this.health, (int) this.y);
		if (this.isSparkAnimate)
		{
			final int sparkY = (GameView.A - SparksImages.h) / 2;
			final int sparkX = (GameView.A - SparksImages.w) / 2;
			final Bitmap sparkBitamp = SparksImages.getAttackBitmap(getNumberSpark(millis));
			canvas.drawBitmap(sparkBitamp, sparkX, sparkY, null);
		}
	}
	
	private void drawDecreaseHealth(Canvas canvas, int decrease, int y)
	{
		if (decrease < 10)
		{
			final int offset = GameView.a * 3;
			canvas.drawBitmap(BigNumberImages.minusBitmap, offset, y, null);
			canvas.drawBitmap(BigNumberImages.getBitmap(decrease),
					BigNumberImages.w * 1 + offset, y, null);
		}
		else
		{
			canvas.drawBitmap(BigNumberImages.minusBitmap, 0, y, null);
			canvas.drawBitmap(BigNumberImages.getBitmap(decrease / 10), BigNumberImages.w * 1, y, null);
			canvas.drawBitmap(BigNumberImages.getBitmap(decrease % 10), BigNumberImages.w * 2, y, null);
		}
	}
	
	private int getNumberSpark(long millis)
	{
		return (int) ((millis - this.startSparkAnimateTime) * 2
		/ (AnimateDecreaseHealthView.TIME_ANIMATION_SPARKS / SparksImages.amountAttack));
	}
	
}
