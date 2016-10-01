package ru.ancientempires.draws.onframes;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ru.ancientempires.draws.BaseDrawMain;

public class DrawBitmap extends DrawOnFrames {

	private Bitmap bitmap;
	private int    y;
	private int    x;

	public DrawBitmap(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public DrawBitmap setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		return this;
	}

	public DrawBitmap setYX(int y, int x) {
		this.y = y;
		this.x = x;
		return this;
	}

	@Override
	public void drawOnFrames(Canvas canvas) {
		canvas.drawBitmap(bitmap, x, y, null);
	}

}
