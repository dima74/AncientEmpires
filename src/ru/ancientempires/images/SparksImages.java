package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.helpers.ZIPHelper;
import android.graphics.Bitmap;

import com.google.gson.stream.JsonReader;

public class SparksImages
{
	
	public static Bitmap[]	bitmapsAttack;
	public static Bitmap[]	bitmapsDefault;
	public static Bitmap[]	bitmapsSmall;
	
	public static int		amountAttack;
	public static int		amountDefault;
	public static int		amountSmall;
	
	public static int		hSmall;
	public static int		wSmall;
	
	public static int		hDefault;
	public static int		wDefault;
	
	public static void preloadResources(ZipFile images, String path) throws IOException
	{
		JsonReader reader = new JsonReader(ZIPHelper.getISR(images, path + "info.json"));
		reader.beginObject();
		String pathAttack = path + JsonHelper.readString(reader, "attack_spark_folder");
		String pathDefault = path + JsonHelper.readString(reader, "default_spark_folder");
		String pathSmall = path + JsonHelper.readString(reader, "small_spark_folder");
		reader.endObject();
		reader.close();
		SparksImages.preloadResourcesAtttack(images, pathAttack);
		SparksImages.preloadResourcesDefault(images, pathDefault);
		SparksImages.preloadResourcesSmall(images, pathSmall);
	}
	
	private static void preloadResourcesAtttack(ZipFile images, String path) throws IOException
	{
		SparksImages.amountAttack = 6;
		SparksImages.bitmapsAttack = new Bitmap[SparksImages.amountAttack];
		for (int i = 0; i < SparksImages.amountAttack; i++)
			SparksImages.bitmapsAttack[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
	}
	
	private static void preloadResourcesDefault(ZipFile images, String path) throws IOException
	{
		SparksImages.amountDefault = 6;
		SparksImages.bitmapsDefault = new Bitmap[SparksImages.amountDefault];
		for (int i = 0; i < SparksImages.amountDefault; i++)
			SparksImages.bitmapsDefault[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
	}
	
	private static void preloadResourcesSmall(ZipFile images, String path) throws IOException
	{
		SparksImages.amountSmall = 3;
		SparksImages.bitmapsSmall = new Bitmap[SparksImages.amountSmall];
		for (int i = 0; i < SparksImages.amountSmall; i++)
			SparksImages.bitmapsSmall[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
		
		SparksImages.hSmall = SparksImages.bitmapsSmall[0].getHeight();
		SparksImages.wSmall = SparksImages.bitmapsSmall[0].getWidth();
		SparksImages.hDefault = SparksImages.bitmapsDefault[0].getHeight();
		SparksImages.wDefault = SparksImages.bitmapsDefault[0].getWidth();
	}
	
}
