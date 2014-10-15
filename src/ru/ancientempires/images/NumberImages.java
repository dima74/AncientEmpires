package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public class NumberImages
{
	
	public static final int	amountNumbers	= 10;
	private static Bitmap[]	numberBitmaps	= new Bitmap[NumberImages.amountNumbers];
	
	public static int		height;
	public static int		width;
	
	public static Bitmap getNumberBitmap(int number)
	{
		return NumberImages.numberBitmaps[number];
	}
	
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		for (int i = 0; i < NumberImages.amountNumbers; i++)
			NumberImages.numberBitmaps[i] = BitmapHelper.getResizeBitmap(imagesZipFile, "numbers/" + i + ".png");
		NumberImages.height = NumberImages.numberBitmaps[0].getHeight();
		NumberImages.width = NumberImages.numberBitmaps[0].getWidth();
	}
}
