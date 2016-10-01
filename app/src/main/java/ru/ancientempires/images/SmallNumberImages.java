package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.FileLoader;

public class SmallNumberImages extends NumberImages {

	public static SmallNumberImages get() {
		return Client.client.images.smallNumber;
	}

	public Bitmap asterisk;
	public Bitmap defenceBitmap;

	@Override
	public void preload(FileLoader loader) throws IOException {
		preloadBase(loader);
		defenceBitmap = loader.loadImage("defence.png");
		asterisk = loader.loadImage("asterisk.png");
	}

}
