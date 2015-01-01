package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public class ArrowsImages
{
	
	public static Bitmap	equals;
	public static Bitmap	more;
	public static Bitmap	less;
	
	public static void preloadResources(ZipFile images, String path) throws IOException
	{
		ArrowsImages.equals = BitmapHelper.getResizeBitmap(images, path + "equals.png");
		ArrowsImages.more = BitmapHelper.getResizeBitmap(images, path + "more.png");
		ArrowsImages.less = BitmapHelper.getResizeBitmap(images, path + "less.png");
	}
	
}
