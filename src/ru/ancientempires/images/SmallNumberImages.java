package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;

public class SmallNumberImages extends NumberImages
{
	
	public static SmallNumberImages	images;
	public static Bitmap			defenceBitmap;
	
	public static void preload(String path) throws IOException
	{
		(SmallNumberImages.images = new SmallNumberImages()).preloadBase(path);
		SmallNumberImages.defenceBitmap = BitmapHelper.getResizeBitmap(path + "defence.png");
	}
	
}
