package ru.ancientempires.helpers;

import helpers.MatrixHelper;
import helpers.ZIPHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import ru.ancientempires.GameView;
import android.content.res.Resources;
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
	
	/*public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth)
		{
			
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth)
				inSampleSize *= 2;
		}
		
		return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
	{
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		
		// Calculate inSampleSize
		options.inSampleSize = BitmapHelper.calculateInSampleSize(options, reqWidth, reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
		
		return bitmap;
	}*/
	
	public static Bitmap getBitmap1(Resources res, int id)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(res, id);
		int height = (int) (bitmap.getHeight() * GameView.baseCellHeightMulti);
		int width = (int) (bitmap.getWidth() * GameView.baseCellWidthMulti);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
		
		return bitmap;
	}
	
	public static Bitmap getCellBitmap1(Resources res, int id)
	{
		Bitmap bitmap = BitmapHelper.getBitmap1(res, id);
		
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
	
	public static Bitmap getResizeBitmap(Bitmap bitmap)
	{
		int height = (int) (bitmap.getHeight() * GameView.baseCellHeightMulti);
		int width = (int) (bitmap.getWidth() * GameView.baseCellWidthMulti);
		bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
		
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
