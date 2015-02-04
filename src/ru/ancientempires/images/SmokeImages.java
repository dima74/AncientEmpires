package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.helpers.ZIPHelper;
import android.graphics.Bitmap;

import com.google.gson.stream.JsonReader;

public class SmokeImages
{
	
	public static Bitmap[]	bitmapsDefault;
	public static Bitmap[]	bitmapsSmall;
	
	public static int		amountDefault;
	public static int		amountSmall;
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		JsonReader reader = new JsonReader(ZIPHelper.getISR(images, path + "info.json"));
		reader.beginObject();
		String pathDefault = path + JsonHelper.readString(reader, "folder_default");
		String pathSmall = path + JsonHelper.readString(reader, "folder_small");
		reader.endObject();
		reader.close();
		SmokeImages.preloadDefault(images, pathDefault);
		SmokeImages.preloadSmall(images, pathSmall);
	}
	
	private static void preloadDefault(ZipFile images, String path) throws IOException
	{
		SmokeImages.amountDefault = 4;
		SmokeImages.bitmapsDefault = new Bitmap[SmokeImages.amountDefault];
		for (int i = 0; i < SmokeImages.amountDefault; i++)
			SmokeImages.bitmapsDefault[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
	}
	
	private static void preloadSmall(ZipFile images, String path) throws IOException
	{
		SmokeImages.amountSmall = 4;
		SmokeImages.bitmapsSmall = new Bitmap[SmokeImages.amountSmall];
		for (int i = 0; i < SmokeImages.amountSmall; i++)
			SmokeImages.bitmapsSmall[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
	}
	
}
