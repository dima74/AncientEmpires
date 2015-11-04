package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;

public class BigNumberImages extends NumberImages
{
	
	public static BigNumberImages	images;
	public static Bitmap			slashBitmap;
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		BigNumberImages.images = new BigNumberImages();
		BigNumberImages.images.plus = BitmapHelper.getResizeBitmap(images, path + "+.png");
		BigNumberImages.slashBitmap = BitmapHelper.getResizeBitmap(images, path + "slash.png");
		BigNumberImages.images.preloadBase(images, path);
	}
	
}
