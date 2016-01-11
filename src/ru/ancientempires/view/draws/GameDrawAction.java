package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import android.graphics.Paint;
import ru.ancientempires.action.handlers.ActionFromTo;
import ru.ancientempires.images.Images;

public class GameDrawAction extends GameDraw
{
	
	public static float	mScale	= 2.5f;
	public static int	mA		= (int) (Images.get().bitmapSize * GameDrawAction.mScale);
	
	public static Paint whiteAlphaPaint = new Paint();
	
	static
	{
		GameDrawAction.whiteAlphaPaint.setColor(0xDDFFFFFF);
	}
	
	private ActionFromTo	action1;
	private ActionFromTo	action2;
	
	public int	h	= ActionImages().h * 2;
	public int	w	= GameDraw.w;
	
	public void start(ActionFromTo action1, ActionFromTo action2)
	{
		this.action1 = action1;
		this.action2 = action2;
	}
	
	public boolean isActive()
	{
		return action1 != null;
	}
	
	public boolean touch(float y, float x)
	{
		if (y < 0 || y > h)
			return false;
		ActionFromTo type = x < w / 2 ? action1 : action2;
		destroy();
		GameDraw.main.inputPlayer.inputUnit.performAction(type);
		return true;
	}
	
	public void destroy()
	{
		action1 = action2 = null;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawRect(0, 0, w, h, GameDrawAction.whiteAlphaPaint);
		int y = (h - ActionImages().h) / 2;
		int delta = w / 3;
		int x1 = w / 2 - delta / 2 - ActionImages().w / 2;
		int x2 = w / 2 + delta / 2 - ActionImages().w / 2;
		canvas.drawBitmap(ActionImages().getActionBitmap(action1), x1, y, null);
		canvas.drawBitmap(ActionImages().getActionBitmap(action2), x2, y, null);
	}
	
}
