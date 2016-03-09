package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.client.Client;

public class StatusesImages extends IImages
{
	
	public static StatusesImages get()
	{
		return Client.client.images.statuses;
	}
	
	public Bitmap	aura;
	public Bitmap	poison;
					
	public int		h;
	public int		w;
					
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		aura = loader.loadImage("aura.png");
		poison = loader.loadImage("poison.png");
		
		h = poison.getHeight();
		w = poison.getWidth();
	}
	
}
