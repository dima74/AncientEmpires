package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;

public class ArrowsImages
{
	
	public static Bitmap	equals;
	public static Bitmap	more;
	public static Bitmap	less;
	
	public static void preload(String path) throws IOException
	{
		ArrowsImages.equals = BitmapHelper.getResizeBitmap(path + "equals.png");
		ArrowsImages.more = BitmapHelper.getResizeBitmap(path + "more.png");
		ArrowsImages.less = BitmapHelper.getResizeBitmap(path + "less.png");
	}
	
}
