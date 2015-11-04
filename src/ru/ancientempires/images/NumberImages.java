package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import ru.ancientempires.helpers.BitmapHelper;

public abstract class NumberImages
{
	
	public Bitmap	minus;
	public Bitmap	plus;
	public Bitmap[]	digits	= new Bitmap[10];
	
	public Bitmap[]	numbers;
	public Bitmap[]	numbersPlus;
	public Bitmap[]	numbersMinus;
	
	public int	h;
	public int	w;
	
	public Bitmap getBitmap(int number)
	{
		return number <= 100 ? this.numbers[number] : createBitmap(number);
	}
	
	public Bitmap createBitmap(int number)
	{
		int copyNumber = number;
		int amountDigits = 1;
		while ((copyNumber /= 10) > 0)
			amountDigits++;
			
		Bitmap bitmap = Bitmap.createBitmap(this.w * amountDigits, this.h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int i = amountDigits - 1; i >= 0; i--, number /= 10)
			canvas.drawBitmap(this.digits[number % 10], i * this.w, 0, null);
		return bitmap;
	}
	
	public Bitmap getBitmap(int number, int sign)
	{
		return sign == 0 ? getBitmap(number) : sign == +1 ? this.numbersPlus[number] : this.numbersMinus[number];
	}
	
	public void preloadBase(ZipFile images, String path) throws IOException
	{
		for (int i = 0; i < 10; i++)
			this.digits[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
		this.minus = BitmapHelper.getResizeBitmap(images, path + "-.png");
		this.h = this.digits[0].getHeight();
		this.w = this.digits[0].getWidth();
		
		this.numbers = getNumbers(null);
		if (this.plus != null)
			this.numbersPlus = getNumbers(this.plus);
		this.numbersMinus = getNumbers(this.minus);
	}
	
	private Bitmap[] getNumbers(Bitmap prefix)
	{
		Bitmap[] bitmaps = new Bitmap[101];
		for (int i = 0; i < 10; i++)
			bitmaps[i] = getBitmap(prefix, this.digits[i]);
		for (int i = 10; i < 100; i++)
			bitmaps[i] = getBitmap(prefix, this.digits[i / 10], this.digits[i % 10]);
		bitmaps[100] = getBitmap(prefix, this.digits[1], this.digits[0], this.digits[0]);
		return bitmaps;
	}
	
	private Bitmap getBitmap(Bitmap b1, Bitmap b2)
	{
		if (b1 == null)
			return b2;
		Bitmap bitmap = Bitmap.createBitmap(this.w * 2, this.h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b1, 0, 0, null);
		canvas.drawBitmap(b2, this.w, 0, null);
		return bitmap;
	}
	
	private Bitmap getBitmap(Bitmap b1, Bitmap b2, Bitmap b3)
	{
		if (b1 == null)
			return getBitmap(b2, b3);
		Bitmap bitmap = Bitmap.createBitmap(this.w * 3, this.h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b1, 0, 0, null);
		canvas.drawBitmap(b2, this.w, 0, null);
		canvas.drawBitmap(b3, this.w * 2, 0, null);
		return bitmap;
	}
	
	private Bitmap getBitmap(Bitmap b1, Bitmap b2, Bitmap b3, Bitmap b4)
	{
		if (b1 == null)
			return getBitmap(b2, b3, b4);
		Bitmap bitmap = Bitmap.createBitmap(this.w * 4, this.h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b1, 0, 0, null);
		canvas.drawBitmap(b2, this.w, 0, null);
		canvas.drawBitmap(b3, this.w * 2, 0, null);
		canvas.drawBitmap(b4, this.w * 3, 0, null);
		return bitmap;
	}
	
}
