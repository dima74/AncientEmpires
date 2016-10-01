package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.FileLoader;

public class SparksImages extends AbstractImages
{

	public static SparksImages get()
	{
		return Client.client.images.sparks;
	}

	public Bitmap[] bitmapsDefault;
	public Bitmap[] bitmapsSmall;
	public Bitmap[] bitmapsAttack;

	public int amountDefault;
	public int amountSmall;
	public int amountAttack;

	public int hDefault;
	public int wDefault;

	public int hSmall;
	public int wSmall;

	@Override
	public void preload(FileLoader loader) throws IOException
	{
		preloadDefault(loader.getLoader("default/"));
		preloadSmall(loader.getLoader("small/"));
		preloadAttack(loader.getLoader("attack/"));
	}

	private void preloadDefault(FileLoader loader) throws IOException
	{
		amountDefault = 6;
		bitmapsDefault = new Bitmap[amountDefault];
		for (int i = 0; i < amountDefault; i++)
			bitmapsDefault[i] = loader.loadImage(i + ".png");
		hDefault = bitmapsDefault[0].getHeight();
		wDefault = bitmapsDefault[0].getWidth();
	}

	private void preloadSmall(FileLoader loader) throws IOException
	{
		amountSmall = 3;
		bitmapsSmall = new Bitmap[amountSmall];
		for (int i = 0; i < amountSmall; i++)
			bitmapsSmall[i] = loader.loadImage(i + ".png");
		hSmall = bitmapsSmall[0].getHeight();
		wSmall = bitmapsSmall[0].getWidth();
	}

	private void preloadAttack(FileLoader loader) throws IOException
	{
		amountAttack = 6;
		bitmapsAttack = new Bitmap[amountAttack];
		for (int i = 0; i < amountAttack; i++)
			bitmapsAttack[i] = loader.loadImage(i + ".png");
	}

}
