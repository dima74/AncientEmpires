package ru.ancientempires.images;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

import java.io.IOException;

import ru.ancientempires.helpers.FileLoader;

public abstract class NumberImages extends AbstractImages
{
	
	public Bitmap minus;
	public Bitmap plus;
	public Bitmap[] digits = new Bitmap[10];
	
	public Bitmap[] numbers;
	public Bitmap[] numbersPlus;
	public Bitmap[] numbersMinus;
	
	public int h;
	public int w;
	
	public Bitmap getBitmap(int number)
	{
		return number <= 100 ? numbers[number] : createBitmap(number);
	}
	
	public Bitmap createBitmap(int number)
	{
		int copyNumber = number;
		int numberDigits = 1;
		while ((copyNumber /= 10) > 0)
			numberDigits++;

		Bitmap bitmap = Bitmap.createBitmap(w * numberDigits, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int i = numberDigits - 1; i >= 0; i--, number /= 10)
			canvas.drawBitmap(digits[number % 10], i * w, 0, null);
		return bitmap;
	}
	
	public Bitmap getBitmap(int number, int sign)
	{
		return sign == 0 ? getBitmap(number) : sign == +1 ? numbersPlus[number] : numbersMinus[number];
	}
	
	public void preloadBase(FileLoader loader) throws IOException
	{
		for (int i = 0; i < 10; i++)
			digits[i] = loader.loadImage(i + ".png");
		minus = loader.loadImage("-.png");
		h = digits[0].getHeight();
		w = digits[0].getWidth();
		
		numbers = getNumbers(null);
		if (plus != null)
			numbersPlus = getNumbers(plus);
		numbersMinus = getNumbers(minus);
	}
	
	private Bitmap[] getNumbers(Bitmap prefix)
	{
		Bitmap[] bitmaps = new Bitmap[101];
		for (int i = 0; i < 10; i++)
			bitmaps[i] = getBitmap(prefix, digits[i]);
		for (int i = 10; i < 100; i++)
			bitmaps[i] = getBitmap(prefix, digits[i / 10], digits[i % 10]);
		bitmaps[100] = getBitmap(prefix, digits[1], digits[0], digits[0]);
		return bitmaps;
	}
	
	private Bitmap getBitmap(Bitmap b1, Bitmap b2)
	{
		if (b1 == null)
			return b2;
		Bitmap bitmap = Bitmap.createBitmap(w * 2, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b1, 0, 0, null);
		canvas.drawBitmap(b2, w, 0, null);
		return bitmap;
	}
	
	private Bitmap getBitmap(Bitmap b1, Bitmap b2, Bitmap b3)
	{
		if (b1 == null)
			return getBitmap(b2, b3);
		Bitmap bitmap = Bitmap.createBitmap(w * 3, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b1, 0, 0, null);
		canvas.drawBitmap(b2, w, 0, null);
		canvas.drawBitmap(b3, w * 2, 0, null);
		return bitmap;
	}
	
	private Bitmap getBitmap(Bitmap b1, Bitmap b2, Bitmap b3, Bitmap b4)
	{
		if (b1 == null)
			return getBitmap(b2, b3, b4);
		Bitmap bitmap = Bitmap.createBitmap(w * 4, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b1, 0, 0, null);
		canvas.drawBitmap(b2, w, 0, null);
		canvas.drawBitmap(b3, w * 2, 0, null);
		canvas.drawBitmap(b4, w * 3, 0, null);
		return bitmap;
	}
	
}
