package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.images.BigNumberImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawNumberSinus extends GameDrawBitmapSinus
{
	
	public static final int FRAMES_ANIMATE = GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES * 3 / 2; // 48 // GameDrawDecreaseHealth.ys.length / 2;
	
	public GameDrawNumberSinus(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawNumberSinus animate(int y, int x, int sign, int number)
	{
		animate(y, x + (GameDraw.A - BigNumberImages.images.getBitmap(number, sign).getWidth()) / 2, BigNumberImages.images.getBitmap(number, sign), 1);
		return this;
	}
	
}
