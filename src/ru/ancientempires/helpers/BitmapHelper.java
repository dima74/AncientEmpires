package ru.ancientempires.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import ru.ancientempires.GameView;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class BitmapHelper
{
	
	public static Bitmap getResizeBitmap(int[][] data)
	{
		int[] dataArray = MatrixHelper.getArrayFromMatrix(data);
		Bitmap bitmap = Bitmap.createBitmap(dataArray, data[0].length, data.length, Config.ARGB_8888);
		bitmap = BitmapHelper.getResizeBitmap(bitmap);
		return bitmap;
	}
	
	public static Bitmap getBitmap(ZipFile zipFile, String zipPath) throws IOException
	{
		InputStream inputStream = ZIPHelper.getIS(zipFile, zipPath);
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		bitmap = BitmapHelper.getResizeBitmap(bitmap);
		return bitmap;
	}
	
	public static Bitmap getBitmap(ZipFile zipFile, String zipPath, float multiHeight, float multiWidth) throws IOException
	{
		InputStream inputStream = ZIPHelper.getIS(zipFile, zipPath);
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		// bitmap = BitmapHelper.getResizeBitmap(bitmap);
		
		int height = (int) (bitmap.getHeight() * multiHeight);
		int width = (int) (bitmap.getWidth() * multiWidth);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		
		return bitmap;
	}
	
	public static Bitmap getResizeBitmap(Bitmap bitmap)
	{
		int height = (int) (bitmap.getHeight() * GameView.baseCellHeightMulti);
		int width = (int) (bitmap.getWidth() * GameView.baseCellWidthMulti);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		
		int bitmapHeight = bitmap.getHeight();
		int bitmapWidth = bitmap.getWidth();
		if (!GameView.isBaseCellSizeDetermine)
		{
			GameView.isBaseCellSizeDetermine = true;
			GameView.baseCellHeight = bitmapHeight;
			GameView.baseCellWidth = bitmapWidth;
		}
		else if (GameView.baseCellHeight != bitmapHeight || GameView.baseCellWidth != bitmapWidth)
		{
			// какая-нибудь норм обработка ошибки тут.
		}
		
		return bitmap;
	}
	
}
