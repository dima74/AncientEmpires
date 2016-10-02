package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

import static ru.ancientempires.draws.DrawAction.mA;

public class DrawInfoNull extends Draw {

	public int a = 2;
	public int h;
	public int w = w();

	private Bitmap backgroundBitmap;

	public DrawInfoNull(BaseDrawMain mainBase) {
		super(mainBase);
		h = mA + 8 * 2;
		backgroundBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(backgroundBitmap);
		drawRect(canvas, 0, 0, w, h, DrawInfo.color4);
		drawRect(canvas, 0, 0, w, a, DrawInfo.color3);
		drawRect(canvas, 0, a, w, a * 2, DrawInfo.color1);
		drawRect(canvas, 0, a * 3, w, a, DrawInfo.color2);
		drawRect(canvas, 0, a * 4, w, a, DrawInfo.color5);

		// 3 1
		// 1 2
		// 2 1
		// 5 1
		// 4 h
	}

	public void drawRect(Canvas canvas, int x, int y, int w, int h, Paint paint) {
		canvas.drawRect(x, y, x + w, y + h, paint);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
	}
}
