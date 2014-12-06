package ru.ancientempires.view;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import android.content.Context;
import android.os.Handler;
import android.widget.FrameLayout;

public class AnimateAttackView extends FrameLayout
{
	
	private static final int	TIME_ANIMATION	= 1548;	// DELAY * 6
														
	private int					attackingY;
	private int					attackingX;
	private int					attackedY;
	private int					attackedX;
	
	private int					attackingDecrease;
	private int					attackedDecrease;
	private boolean				attackingIsLive;
	private boolean				attackedIsLive;
	
	private AnimateDecreaseHealthView	directView, inverseView;
	
	public AnimateAttackView(Context context)
	{
		super(context);
		setClipChildren(false);
		addView(this.directView = new AnimateDecreaseHealthView(getContext()));
		addView(this.inverseView = new AnimateDecreaseHealthView(getContext()));
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
		
		this.directView.startAnimate(this.attackedY, this.attackedX, this.attackedDecrease);
		
		if (this.attackingDecrease > 0)
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					startInverseAnimate();
				}
			}, AnimateAttackView.TIME_ANIMATION * 2 / 3);
	}
	
	private void startInverseAnimate()
	{
		this.inverseView.startAnimate(this.attackingY, this.attackingX, this.attackingDecrease);
	}
	
}
