package ru.ancientempires.helpers;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.view.GameView;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class BitmapHelper
{
	
	public static Bitmap getBitmap(ZipFile zipFile, String zipPath) throws IOException
	{
		return BitmapFactory.decodeStream(ZIPHelper.getIS(zipFile, zipPath));
	}
	
	public static Bitmap getMultiBitmap(ZipFile zipFile, String zipPath, float multiHeight, float multiWidth) throws IOException
	{
		Bitmap bitmap = BitmapHelper.getBitmap(zipFile, zipPath);
		return BitmapHelper.getMultiBitmap(bitmap, multiHeight, multiWidth);
	}
	
	public static Bitmap getMultiBitmap(Bitmap bitmap, float multiHeight, float multiWidth)
	{
		int height = (int) (bitmap.getHeight() * multiHeight);
		int width = (int) (bitmap.getWidth() * multiWidth);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		
		return bitmap;
	}
	
	public static Bitmap getResizeBitmap(int[][] data)
	{
		int[] dataArray = MatrixHelper.getArrayFromMatrix(data);
		Bitmap bitmap = Bitmap.createBitmap(dataArray, data[0].length, data.length, Config.ARGB_8888);
		bitmap = BitmapHelper.getResizeBitmap(bitmap);
		return bitmap;
	}
	
	public static Bitmap getResizeBitmap(ZipFile zipFile, String zipPath) throws IOException
	{
		Bitmap bitmap = BitmapHelper.getBitmap(zipFile, zipPath);
		bitmap = BitmapHelper.getResizeBitmap(bitmap);
		return bitmap;
	}
	
	public static Bitmap getResizeBitmap(Bitmap bitmap)
	{
		bitmap = BitmapHelper.getMultiBitmap(bitmap, GameView.baseHMulti, GameView.baseWMulti);
		
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
