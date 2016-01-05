package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.view.draws.GameDraw;

public class GameDrawNumberSinus extends GameDrawBitmapSinus
{
	
	public static final int FRAMES_ANIMATE = GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES * 3 / 2; // 48 // GameDrawDecreaseHealth.ys.length / 2;
	
	public GameDrawNumberSinus animate(int y, int x, int sign, int number)
	{
		animate(y, x + (GameDraw.A - BigNumberImages().getBitmap(number, sign).getWidth()) / 2, BigNumberImages().getBitmap(number, sign), 1);
		return this;
	}
	
}
