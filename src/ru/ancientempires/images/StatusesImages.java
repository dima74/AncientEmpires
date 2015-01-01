package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public class StatusesImages
{
	
	public static Bitmap	aura;
	public static Bitmap	poison;
	
	public static int		h;
	public static int		w;
	
	public static void preloadResources(ZipFile images, String path) throws IOException
	{
		StatusesImages.aura = BitmapHelper.getResizeBitmap(images, path + "aura.png");
		StatusesImages.poison = BitmapHelper.getResizeBitmap(images, path + "poison.png");
		
		StatusesImages.h = StatusesImages.poison.getHeight();
		StatusesImages.w = StatusesImages.poison.getWidth();
	}
}
