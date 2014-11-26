package ru.ancientempires.view;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.BigNumberImages;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimateAttackView extends View
{
	
	private static final int	TIME_ANIMATION		= 1548;
	
	private float[]				ys;
	private float				directY;
	private float				reverseY;
	
	private int					attackingY;
	private int					attackingX;
	private int					attackedY;
	private int					attackedX;
	
	private int					attackingDecrease;
	private int					attackedDecrease;
	private boolean				attackingIsLive;
	private boolean				attackedIsLive;
	
	private boolean				isAnimateDirect		= false;
	private boolean				isAnimateReverse	= false;
	
	public AnimateAttackView(Context context)
	{
		super(context);
		/*
		this.ys = new float[]
		{
				// 9, 8.5f, 6, 4.5f, 3, 3, 13, 13, 4, 4, 6, 11, 13, 13, 13, 11, 10, 10, 10, 10, 11,
				9, 7, 3.5f, 3.5f, 5, 10, 13, 13, 4, 4, 6, 11, 13, 13, 13, 11, 10, 10, 10, 10, 11,
				12, 13, 13, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13
		};
		*/
		this.ys = new float[]
		{
				9, 9, 9, 9, 6, 6, 6, 6, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3,
				3, 4, 4, 4, 4, 6, 6, 6, 9, 9, 9, 9, 13, 13, 13, 13, 11,
				11, 11, 11, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11,
				11, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12, 13,
				13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
				13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13
		};
	}
	
	public void showAnimate(Action action, ActionResult result)
	{
		this.attackingY = (int) action.getProperty("attackingI") * GameView.A;
		this.attackingX = (int) action.getProperty("attackingJ") * GameView.A;
		this.attackedY = (int) action.getProperty("attackedI") * GameView.A;
		this.attackedX = (int) action.getProperty("attackedJ") * GameView.A;
		
		this.attackingDecrease = (int) (float) result.getProperty("attackingDecrease");
		this.attackedDecrease = (int) (float) result.getProperty("attackedDecrease");
		this.attackingIsLive = (boolean) result.getProperty("attackingLive");
		this.attackedIsLive = (boolean) result.getProperty("attackedLive");
		
		this.isAnimateDirect = true;
		
		ValueAnimator animator = ValueAnimator.ofFloat(this.ys);
		animator.setDuration(AnimateAttackView.TIME_ANIMATION);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(new AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				updateAnimationTime(animation.getCurrentPlayTime());
				AnimateAttackView.this.directY = (float) animation.getAnimatedValue() * GameView.a;
				MyLog.l("d", AnimateAttackView.this.directY);
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
				AnimateAttackView.this.isAnimateDirect = false;
			}
			
			@Override
			public void onAnimationCancel(Animator animation)
			{}
		});
		animator.start();
	}
	
	private void updateAnimationTime(long time)
	{
		if (!this.isAnimateReverse && this.attackingDecrease > 0
			&& time > AnimateAttackView.TIME_ANIMATION * 2 / 3)
		{
			this.isAnimateReverse = true;
			ValueAnimator animator = ValueAnimator.ofFloat(this.ys);
			animator.setDuration(AnimateAttackView.TIME_ANIMATION);
			animator.setInterpolator(new LinearInterpolator());
			animator.addUpdateListener(new AnimatorUpdateListener()
			{
				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					AnimateAttackView.this.reverseY = (float) animation.getAnimatedValue() * GameView.a;
					MyLog.l("r", AnimateAttackView.this.reverseY);
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
					AnimateAttackView.this.isAnimateReverse = false;
				}
				
				@Override
				public void onAnimationCancel(Animator animation)
				{}
			});
			animator.start();
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.translate(this.attackedX, this.attackedY);
		if (this.isAnimateDirect)
			drawDecreaseHealth(canvas, this.attackedDecrease, (int) this.directY);
		// draw attack искру
		
		canvas.translate(this.attackingX - this.attackedX, this.attackingY - this.attackedY);
		if (this.isAnimateReverse)
			drawDecreaseHealth(canvas, this.attackingDecrease, (int) this.reverseY);
	}
	
	private void drawDecreaseHealth(Canvas canvas, int decrease, int y)
	{
		canvas.drawBitmap(BigNumberImages.minusBitmap, 0, y, null);
		canvas.drawBitmap(BigNumberImages.getBitmap(decrease / 10), BigNumberImages.w * 1, y, null);
		canvas.drawBitmap(BigNumberImages.getBitmap(decrease % 10), BigNumberImages.w * 2, y, null);
	}
	
}
