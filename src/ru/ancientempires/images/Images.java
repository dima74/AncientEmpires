package ru.ancientempires.images;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.ArrayHelper;
import ru.ancientempires.helpers.ColorHelper;
import ru.ancientempires.helpers.ZIPHelper;
import ru.ancientempires.model.Game;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Images
{
	
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		CellImages.preloadResources(imagesZipFile);
		ActionImages.preloadResources(imagesZipFile);
		NumberImages.preloadResources(imagesZipFile);
	}
	
	public static void loadResources(ZipFile imagesZipFile, Game game) throws IOException
	{
		CellImages.loadResources(imagesZipFile, game);
		UnitImages.loadResources(imagesZipFile, game);
	}
	
	public static int[][] getMatrixDataImage(ZipFile imagesZipFile, String path) throws IOException
	{
		Bitmap bitmap = BitmapFactory.decodeStream(ZIPHelper.getIS(imagesZipFile, path));
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		IntBuffer buffer = IntBuffer.allocate(height * width);
		bitmap.copyPixelsToBuffer(buffer);
		int[] dataArray = buffer.array();
		
		int[][] data = ArrayHelper.getMatrixFromArray(dataArray, height, width);
		
		// TODO если вот тут BitmapFactory.decodeStream в Options поставить inPreMultiPlied (API 19) в false, то вроде можно без костыля
		Images.costil(data);
		
		return data;
	}
	
	private static void costil(int[][] data)
	{
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[0].length; j++)
				data[i][j] = ColorHelper.toNormalColor(data[i][j]);
	}
	
}
