package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.images.BigNumberImages;
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
	
	public void animate(int frameToStart, int y, int x, int decreaseHealth)
	{
		super.animate(frameToStart, GameDrawDecreaseHealth.FRAMES_ANIMATE);
		setBitmap(createBitmap(decreaseHealth, y, x));
	}
	
	private Bitmap createBitmap(int number, int y, int x)
	{
		int copyNumber = number;
		int kDigit = 1;
		while ((copyNumber /= 10) > 0)
			kDigit++;
		
		int numberW = BigNumberImages.w * (kDigit + 1);
		Bitmap bitmap = Bitmap.createBitmap(numberW, BigNumberImages.h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		
		canvas.drawBitmap(BigNumberImages.minusBitmap, 0, 0, null);
		for (int i = kDigit; i > 0; i--)
		{
			canvas.drawBitmap(BigNumberImages.getBitmap(number % 10), i * BigNumberImages.w, 0, null);
			number /= 10;
		}
		
		setCoord(y, x + (GameDraw.A - numberW) / 2);
		return bitmap;
	}
	
}
