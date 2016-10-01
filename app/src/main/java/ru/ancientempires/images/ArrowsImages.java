package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.framework.FileLoader;

public class ArrowsImages extends AbstractImages {

	public Bitmap equals;
	public Bitmap more;
	public Bitmap less;

	@Override
	public void preload(FileLoader loader) throws IOException {
		equals = loader.loadImage("equals.png");
		more = loader.loadImage("more.png");
		less = loader.loadImage("less.png");
	}
}
