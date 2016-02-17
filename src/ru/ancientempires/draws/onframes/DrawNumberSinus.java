package ru.ancientempires.draws.onframes;

public class DrawNumberSinus extends DrawBitmapSinus
{
	
	public static final int FRAMES_ANIMATE = DrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES * 3 / 2; // 48 // GameDrawDecreaseHealth.ys.length / 2;
	
	public DrawNumberSinus animate(int y, int x, int sign, int number)
	{
		animate(y, x + (A - BigNumberImages().getBitmap(number, sign).getWidth()) / 2, BigNumberImages().getBitmap(number, sign), 1);
		return this;
	}
	
}
