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
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		for (int i = 0; i < BigNumberImages.amountNumbers; i++)
			BigNumberImages.numberBitmaps[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
		BigNumberImages.minusBitmap = BitmapHelper.getResizeBitmap(images, path + "-.png");
		BigNumberImages.plusBitmap = BitmapHelper.getResizeBitmap(images, path + "+.png");
		BigNumberImages.slashBitmap = BitmapHelper.getResizeBitmap(images, path + "slash.png");
		BigNumberImages.h = BigNumberImages.numberBitmaps[0].getHeight();
		BigNumberImages.w = BigNumberImages.numberBitmaps[0].getWidth();
	}
	
}
