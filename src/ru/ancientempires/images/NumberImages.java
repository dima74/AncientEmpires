package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public class NumberImages
{
	
	public static final int			amountNumbers	= 10;
	private static final Bitmap[]	numberBitmaps	= new Bitmap[NumberImages.amountNumbers];
	public static Bitmap			minusBitmap;
	public static Bitmap			defenceBitmap;
	
	public static int				h;
	public static int				w;
	
	public static Bitmap getNumberBitmap(int number)
	{
		return NumberImages.numberBitmaps[number];
	}
	
	public static void preloadResources(ZipFile images, String path) throws IOException
	{
		for (int i = 0; i < NumberImages.amountNumbers; i++)
			NumberImages.numberBitmaps[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
		NumberImages.minusBitmap = BitmapHelper.getResizeBitmap(images, path + "-.png");
		NumberImages.defenceBitmap = BitmapHelper.getResizeBitmap(images, path + "defence.png");
		NumberImages.h = NumberImages.numberBitmaps[0].getHeight();
		NumberImages.w = NumberImages.numberBitmaps[0].getWidth();
	}
	
}
