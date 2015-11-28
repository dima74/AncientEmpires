package ru.ancientempires.helpers;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelper
{
	
	public static Bitmap getBitmap(String path) throws IOException
	{
		return BitmapFactory.decodeStream(ImagesFileHelper.openIS(path));
	}
	
	public static Bitmap getMultiBitmap(Bitmap bitmap, float multi)
	{
		int height = (int) (bitmap.getHeight() * multi);
		int width = (int) (bitmap.getWidth() * multi);
		return Bitmap.createScaledBitmap(bitmap, width, height, false);
	}
	
	public static Bitmap getResizeBitmap(String path) throws IOException
	{
		return BitmapHelper.getBitmap(path);
	}
	
}
