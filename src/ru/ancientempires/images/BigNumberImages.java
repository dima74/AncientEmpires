package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public class BigNumberImages extends NumberImages
{
	
	public static BigNumberImages	images;
	public static Bitmap			slashBitmap;
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		(BigNumberImages.images = new BigNumberImages()).preloadBase(images, path);
		BigNumberImages.images.plusBitmap = BitmapHelper.getResizeBitmap(images, path + "+.png");
		BigNumberImages.slashBitmap = BitmapHelper.getResizeBitmap(images, path + "slash.png");
	}
	
}
