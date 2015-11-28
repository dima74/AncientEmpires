package ru.ancientempires.images;

import java.io.IOException;

import com.google.gson.stream.JsonReader;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.ImagesFileHelper;
import ru.ancientempires.helpers.JsonHelper;

public class SmokeImages
{
	
	public static Bitmap[]	bitmapsDefault;
	public static Bitmap[]	bitmapsSmall;
	
	public static int	amountDefault;
	public static int	amountSmall;
	
	public static int	hDefault;
	public static int	wDefault;
	
	public static int	hSmall;
	public static int	wSmall;
	
	public static void preload(String path) throws IOException
	{
		JsonReader reader = ImagesFileHelper.getReader(path + "info.json");
		reader.beginObject();
		String pathDefault = path + JsonHelper.readString(reader, "folder_default");
		String pathSmall = path + JsonHelper.readString(reader, "folder_small");
		reader.endObject();
		reader.close();
		SmokeImages.preloadDefault(pathDefault);
		SmokeImages.preloadSmall(pathSmall);
	}
	
	private static void preloadDefault(String path) throws IOException
	{
		SmokeImages.amountDefault = 4;
		SmokeImages.bitmapsDefault = new Bitmap[SmokeImages.amountDefault];
		for (int i = 0; i < SmokeImages.amountDefault; i++)
			SmokeImages.bitmapsDefault[i] = BitmapHelper.getResizeBitmap(path + i + ".png");
		SmokeImages.hDefault = SmokeImages.bitmapsDefault[0].getHeight();
		SmokeImages.wDefault = SmokeImages.bitmapsDefault[0].getWidth();
	}
	
	private static void preloadSmall(String path) throws IOException
	{
		SmokeImages.amountSmall = 4;
		SmokeImages.bitmapsSmall = new Bitmap[SmokeImages.amountSmall];
		for (int i = 0; i < SmokeImages.amountSmall; i++)
			SmokeImages.bitmapsSmall[i] = BitmapHelper.getResizeBitmap(path + i + ".png");
		SmokeImages.hSmall = SmokeImages.bitmapsSmall[0].getHeight();
		SmokeImages.wSmall = SmokeImages.bitmapsSmall[0].getWidth();
	}
	
}
