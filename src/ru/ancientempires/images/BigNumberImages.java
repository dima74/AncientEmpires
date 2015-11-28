package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;

public class BigNumberImages extends NumberImages
{
	
	public static BigNumberImages	images;
	public static Bitmap			slashBitmap;
	
	public static void preload(String path) throws IOException
	{
		BigNumberImages.images = new BigNumberImages();
		BigNumberImages.images.plus = BitmapHelper.getResizeBitmap(path + "+.png");
		BigNumberImages.slashBitmap = BitmapHelper.getResizeBitmap(path + "slash.png");
		BigNumberImages.images.preloadBase(path);
	}
	
}
