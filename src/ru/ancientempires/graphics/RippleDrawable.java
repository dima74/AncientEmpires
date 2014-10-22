package ru.ancientempires.graphics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;

public class RippleDrawable extends Drawable implements View.OnTouchListener
{
	
	final static Property<RippleDrawable, Float>	CREATE_TOUCH_RIPPLE		=
																				new FloatProperty<RippleDrawable>("createTouchRipple")
																				{
																					@Override
																					public void setValue(RippleDrawable object, float value)
																					{
																						object.createTouchRipple(value);
																					}
																					
																					@Override
																					public Float get(RippleDrawable object)
																					{
																						return object.getAnimationState();
																					}
																				};
	
	final static Property<RippleDrawable, Float>	DESTROY_TOUCH_RIPPLE	=
																				new FloatProperty<RippleDrawable>("destroyTouchRipple")
																				{
																					@Override
																					public void setValue(RippleDrawable object, float value)
																					{
																						object.destroyTouchRipple(value);
																					}
																					
																					@Override
																					public Float get(RippleDrawable object)
																					{
																						return object.getAnimationState();
																					}
																				};
	
	final static int								DEFAULT_ANIM_DURATION	= 250;
	final static float								END_RIPPLE_TOUCH_RADIUS	= 150f;
	final static float								END_SCALE				= 1.3f;
	
	final static int								RIPPLE_TOUCH_MIN_ALPHA	= 40;
	final static int								RIPPLE_TOUCH_MAX_ALPHA	= 120;
	final static int								RIPPLE_BACKGROUND_ALPHA	= 100;
	
	Paint											mRipplePaint			= new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint											mRippleBackgroundPaint	= new Paint(Paint.ANTI_ALIAS_FLAG);
	
	Circle											mTouchRipple;
	Circle											mBackgroundRipple;
	
	ObjectAnimator									mCurrentAnimator;
	
	Drawable										mOriginalBackground;
	
	public RippleDrawable()
	{
		initRippleElements();
	}
	
	/**
	 * Creates ripple effect to view
	 *
	 * @param v
	 *            view to apply ripple drawable
	 * @param primaryColor
	 *            color of ripples
	 */
	public static void createRipple(View v, int primaryColor)
	{
		RippleDrawable rippleDrawable = new RippleDrawable();
		rippleDrawable.setDrawable(v.getBackground());
		rippleDrawable.setColor(primaryColor);
		rippleDrawable.setBounds(v.getPaddingLeft(), v.getPaddingTop(),
				v.getPaddingRight(), v.getPaddingBottom());
		
		v.setOnTouchListener(rippleDrawable);
		if (Build.VERSION.SDK_INT >= 16)
			v.setBackground(rippleDrawable);
		else
			// noinspection deprecation
			v.setBackgroundDrawable(rippleDrawable);
	}
	
	/**
	 * Creates ripple animation to view, difference between {@code createRipple(v, primaryColor)} here animation will start automatically without user touch event
	 *
	 * @param x
	 *            fake touch coordinate x
	 * @param y
	 *            fake touch coordinate y
	 * @param v
	 *            view to apply ripple drawable
	 * @param primaryColor
	 *            ripple animation color
	 */
	public static void createRipple(int x, int y, View v, int primaryColor)
	{
		if (!(v.getBackground() instanceof RippleDrawable))
			RippleDrawable.createRipple(v, primaryColor);
		RippleDrawable drawable = (RippleDrawable) v.getBackground();
		drawable.setColor(primaryColor);
		drawable.onFingerDown(v, x, y);
	}
	
	/**
	 * Set colors of ripples
	 *
	 * @param primaryColor
	 *            color of ripples
	 */
	public void setColor(int primaryColor)
	{
		this.mRippleBackgroundPaint.setColor(primaryColor);
		this.mRippleBackgroundPaint.setAlpha(RippleDrawable.RIPPLE_BACKGROUND_ALPHA);
		this.mRipplePaint.setColor(primaryColor);
		
		invalidateSelf();
	}
	
	/**
	 * set first layer you background drawable
	 *
	 * @param drawable
	 *            original background
	 */
	public void setDrawable(Drawable drawable)
	{
		this.mOriginalBackground = drawable;
		
		invalidateSelf();
	}
	
	void initRippleElements()
	{
		this.mTouchRipple = new Circle();
		this.mBackgroundRipple = new Circle();
		
		this.mRipplePaint.setStyle(Paint.Style.FILL);
		this.mRippleBackgroundPaint.setStyle(Paint.Style.FILL);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (this.mOriginalBackground != null)
		{
			this.mOriginalBackground.setBounds(getBounds());
			this.mOriginalBackground.draw(canvas);
		}
		
		this.mBackgroundRipple.draw(canvas, this.mRippleBackgroundPaint);
		this.mTouchRipple.draw(canvas, this.mRipplePaint);
	}
	
	@Override
	public void setAlpha(int alpha)
	{
	}
	
	@Override
	public void setColorFilter(ColorFilter cf)
	{
	}
	
	@Override
	public int getOpacity()
	{
		return 0;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		final int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				onFingerDown(v, event.getX(), event.getY());
				v.onTouchEvent(event); // Fix for views, to handle clicks
				return true; // fix for scroll, when ACTION_UP & ACTION_CANCEL not come
				
			case MotionEvent.ACTION_MOVE:
				onFingerMove(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				onFingerUp();
				break;
		}
		return false;
	}
	
	int	mViewSize	= 0;
	
	void onFingerDown(View v, float x, float y)
	{
		this.mTouchRipple.cx = this.mBackgroundRipple.cx = x;
		this.mTouchRipple.cy = this.mBackgroundRipple.cy = y;
		this.mTouchRipple.radius = this.mBackgroundRipple.radius = 0f;
		this.mViewSize = Math.max(v.getWidth(), v.getHeight());
		
		if (this.mCurrentAnimator == null)
		{
			this.mRippleBackgroundPaint.setAlpha(RippleDrawable.RIPPLE_BACKGROUND_ALPHA);
			
			this.mCurrentAnimator = ObjectAnimator.ofFloat(this, RippleDrawable.CREATE_TOUCH_RIPPLE, 0f, 1f);
			this.mCurrentAnimator.setDuration(RippleDrawable.DEFAULT_ANIM_DURATION);
		}
		
		if (!this.mCurrentAnimator.isRunning())
			this.mCurrentAnimator.start();
	}
	
	void createTouchRipple(float value)
	{
		this.mAnimationValue = value;
		
		this.mTouchRipple.radius = 40f + this.mAnimationValue * (RippleDrawable.END_RIPPLE_TOUCH_RADIUS - 40f);
		this.mBackgroundRipple.radius = this.mAnimationValue * (this.mViewSize * RippleDrawable.END_SCALE);
		
		int min = RippleDrawable.RIPPLE_TOUCH_MIN_ALPHA;
		int max = RippleDrawable.RIPPLE_TOUCH_MAX_ALPHA;
		int alpha = min + (int) (this.mAnimationValue * (max - min));
		this.mRipplePaint.setAlpha(max + min - alpha);
		
		invalidateSelf();
	}
	
	float	mAnimationValue;
	
	void destroyTouchRipple(float value)
	{
		this.mAnimationValue = value;
		
		this.mTouchRipple.radius = RippleDrawable.END_RIPPLE_TOUCH_RADIUS + this.mAnimationValue * (this.mViewSize * RippleDrawable.END_SCALE);
		
		this.mRipplePaint.setAlpha((int) (RippleDrawable.RIPPLE_TOUCH_MIN_ALPHA - this.mAnimationValue * RippleDrawable.RIPPLE_TOUCH_MIN_ALPHA));
		this.mRippleBackgroundPaint.setAlpha
				((int) (RippleDrawable.RIPPLE_BACKGROUND_ALPHA - this.mAnimationValue * RippleDrawable.RIPPLE_BACKGROUND_ALPHA));
		
		invalidateSelf();
	}
	
	float getAnimationState()
	{
		return this.mAnimationValue;
	}
	
	void onFingerUp()
	{
		if (this.mCurrentAnimator != null)
		{
			this.mCurrentAnimator.end();
			this.mCurrentAnimator = null;
			createTouchRipple(RippleDrawable.END_RIPPLE_TOUCH_RADIUS);
		}
		
		this.mCurrentAnimator = ObjectAnimator.ofFloat(this, RippleDrawable.DESTROY_TOUCH_RIPPLE, 0f, 1f);
		this.mCurrentAnimator.setDuration(RippleDrawable.DEFAULT_ANIM_DURATION);
		this.mCurrentAnimator.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				super.onAnimationEnd(animation);
				RippleDrawable.this.mCurrentAnimator = null;
			}
		});
		this.mCurrentAnimator.start();
	}
	
	void onFingerMove(float x, float y)
	{
		this.mTouchRipple.cx = x;
		this.mTouchRipple.cy = y;
		
		invalidateSelf();
	}
	
	@Override
	public boolean setState(int[] stateSet)
	{
		if (this.mOriginalBackground != null)
			return this.mOriginalBackground.setState(stateSet);
		return super.setState(stateSet);
	}
	
	@Override
	public int[] getState()
	{
		if (this.mOriginalBackground != null)
			return this.mOriginalBackground.getState();
		return super.getState();
	}
	
	final static class Circle
	{
		float	cx;
		float	cy;
		float	radius;
		
		public void draw(Canvas canvas, Paint paint)
		{
			canvas.drawCircle(this.cx, this.cy, this.radius, paint);
		}
	}
	
}
