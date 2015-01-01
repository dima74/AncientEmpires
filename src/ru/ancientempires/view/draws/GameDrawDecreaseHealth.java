package ru.ancientempires.view.draws;

import ru.ancientempires.images.BigNumberImages;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

public class GameDrawDecreaseHealth extends GameDrawSinus
{
	
	// length = 96
	private static final int[]	ys				= new int[]
												{
			9, 9, 9, 9, 6, 6, 6, 6, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4,
			4, 6, 6, 6, 6, 9, 9, 9, 9, 13, 13, 13, 13, 11, 11, 11, 11, 10, 10, 10,
			10, 10, 10, 10, 10, 11, 11, 11, 11, 13, 13, 13, 13, 12, 12, 12, 12, 12,
			12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
			13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13
												};
	
	public static final int		FRAMES_ANIMATE	= GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES * 3 / 2;	// 48 // GameDrawDecreaseHealth.ys.length / 2;
																											
	public int					y;
	public int					x;
	
	public int					decreaseHealth;
	private Bitmap				bitmap;
	
	public GameDrawDecreaseHealth(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void animate(int frameToStart, int y, int x, int decreaseHealth)
	{
		super.animate(frameToStart, GameDrawDecreaseHealth.FRAMES_ANIMATE);
		
		this.y = y;
		this.x = x;
		this.decreaseHealth = decreaseHealth;
		this.bitmap = createBitmap(decreaseHealth);
	}
	
	private Bitmap createBitmap(int number)
	{
		int copyNumber = number;
		int kDigit = 1;
		while ((copyNumber /= 10) > 0)
			kDigit++;
		
		int numberW = BigNumberImages.w * (kDigit + 1);
		this.bitmap = Bitmap.createBitmap(numberW, BigNumberImages.h, Config.ARGB_8888);
		Canvas canvas = new Canvas(this.bitmap);
		
		canvas.drawBitmap(BigNumberImages.minusBitmap, 0, 0, null);
		for (int i = kDigit; i > 0; i--)
		{
			canvas.drawBitmap(BigNumberImages.getBitmap(number % 10), i * BigNumberImages.w, 0, null);
			number /= 10;
		}
		
		this.x += (GameDraw.A - numberW) / 2;
		return this.bitmap;
	}
	
	@Override
	public void drawChild(Canvas canvas, int i)
	{
		canvas.drawBitmap(this.bitmap, this.x, this.y, null);
	}
	
}
