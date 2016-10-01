package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.FileLoader;

public class StatusesImages extends AbstractImages
{

	public static StatusesImages get()
	{
		return Client.client.images.statuses;
	}

	public Bitmap aura;
	public Bitmap poison;

	public int h;
	public int w;

	@Override
	public void preload(FileLoader loader) throws IOException
	{
		aura = loader.loadImage("aura.png");
		poison = loader.loadImage("poison.png");

		h = poison.getHeight();
		w = poison.getWidth();
	}

}
