package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.images.BigNumberImages;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

public class GameDrawDecreaseHealth extends GameDrawBitmapSinus
{
	
	public static final int	FRAMES_ANIMATE	= GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES * 3 / 2;	// 48 // GameDrawDecreaseHealth.ys.length / 2;
																										
	public GameDrawDecreaseHealth(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void animate(int frameToStart, int y, int x, int sign, int decreaseHealth)
	{
		super.animate(frameToStart, GameDrawDecreaseHealth.FRAMES_ANIMATE);
		Bitmap bitmap = GameDrawDecreaseHealth.createBitmap(BigNumberImages.images, sign, decreaseHealth);
		setCoord(y, x + (GameDraw.A - bitmap.getWidth()) / 2);
		setBitmap(bitmap);
	}
	
	public static Bitmap createBitmap(NumberImages images, int sign, int number)
	{
		int copyNumber = number;
		int kDigit = 1;
		while ((copyNumber /= 10) > 0)
			kDigit++;
		
		int numberW = images.w * (Math.abs(sign) + kDigit);
		Bitmap bitmap = Bitmap.createBitmap(numberW, images.h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		
		if (sign == -1)
			canvas.drawBitmap(images.minusBitmap, 0, 0, null);
		if (sign == +1)
			canvas.drawBitmap(images.plusBitmap, 0, 0, null);
		int add = Math.abs(sign);
		for (int i = kDigit + add - 1; i >= add; i--)
		{
			canvas.drawBitmap(images.getBitmap(number % 10), i * images.w, 0, null);
			number /= 10;
		}
		
		return bitmap;
	}
	
}
