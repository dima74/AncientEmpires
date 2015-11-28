package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;

public class StatusesImages
{
	
	public static Bitmap	aura;
	public static Bitmap	poison;
	
	public static int	h;
	public static int	w;
	
	public static void preload(String path) throws IOException
	{
		StatusesImages.aura = BitmapHelper.getResizeBitmap(path + "aura.png");
		StatusesImages.poison = BitmapHelper.getResizeBitmap(path + "poison.png");
		
		StatusesImages.h = StatusesImages.poison.getHeight();
		StatusesImages.w = StatusesImages.poison.getWidth();
	}
}
