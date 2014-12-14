package ru.ancientempires.helpers;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.view.GameView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHelper
{
	
	public static Bitmap getBitmap(ZipFile file, String path) throws IOException
	{
		return BitmapFactory.decodeStream(ZIPHelper.getIS(file, path));
	}
	
	public static Bitmap getMultiBitmap(ZipFile file, String path, float multi) throws IOException
	{
		Bitmap bitmap = BitmapHelper.getBitmap(file, path);
		return BitmapHelper.getMultiBitmap(bitmap, multi);
	}
	
	public static Bitmap getMultiBitmap(Bitmap bitmap, float multi)
	{
		int height = (int) (bitmap.getHeight() * multi);
		int width = (int) (bitmap.getWidth() * multi);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		
		return bitmap;
	}
	
	public static Bitmap getResizeBitmap(ZipFile file, String path) throws IOException
	{
		Bitmap bitmap = BitmapHelper.getBitmap(file, path);
		bitmap = BitmapHelper.getResizeBitmap(bitmap);
		return bitmap;
	}
	
	public static Bitmap getResizeBitmap(Bitmap bitmap)
	{
		bitmap = BitmapHelper.getMultiBitmap(bitmap, GameView.baseMulti);
		
		int bitmapHeight = bitmap.getHeight();
		int bitmapWidth = bitmap.getWidth();
		if (!GameView.isBaseCellSizeDetermine)
		{
			GameView.isBaseCellSizeDetermine = true;
			GameView.baseH = bitmapHeight;
			GameView.baseW = bitmapWidth;
			GameView.A = bitmapHeight;
			GameView.a = bitmapHeight / 24;
		}
		else if (GameView.baseH != bitmapHeight || GameView.baseW != bitmapWidth)
			// какая-нибудь норм обработка ошибки тут.
			assert true;
		
		return bitmap;
	}
	
}
