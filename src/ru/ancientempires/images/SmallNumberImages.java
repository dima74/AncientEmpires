package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;

public class SmallNumberImages extends NumberImages
{
	
	public static SmallNumberImages	images;
	public static Bitmap			defenceBitmap;
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		(SmallNumberImages.images = new SmallNumberImages()).preloadBase(images, path);
		SmallNumberImages.defenceBitmap = BitmapHelper.getResizeBitmap(images, path + "defence.png");
	}
	
}
