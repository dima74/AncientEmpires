package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public class BigNumberImages
{
	public static final int			amountNumbers	= 10;
	private static final Bitmap[]	numberBitmaps	= new Bitmap[BigNumberImages.amountNumbers];
	public static Bitmap			minusBitmap;
	public static Bitmap			plusBitmap;
	public static Bitmap			slashBitmap;
	
	public static int				h;
	public static int				w;
	
	public static Bitmap getBitmap(int number)
	{
		return BigNumberImages.numberBitmaps[number];
	}
	
	public static void preloadResources(ZipFile images) throws IOException
	{
		for (int i = 0; i < BigNumberImages.amountNumbers; i++)
			BigNumberImages.numberBitmaps[i] = BitmapHelper.getResizeBitmap(images, "big_numbers/" + i + ".png");
		BigNumberImages.minusBitmap = BitmapHelper.getResizeBitmap(images, "big_numbers/-.png");
		BigNumberImages.plusBitmap = BitmapHelper.getResizeBitmap(images, "big_numbers/+.png");
		BigNumberImages.slashBitmap = BitmapHelper.getResizeBitmap(images, "big_numbers/slash.png");
		BigNumberImages.h = BigNumberImages.numberBitmaps[0].getHeight();
		BigNumberImages.w = BigNumberImages.numberBitmaps[0].getWidth();
	}
	
}
