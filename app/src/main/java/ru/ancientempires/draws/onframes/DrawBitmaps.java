package ru.ancientempires.draws.onframes;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ru.ancientempires.draws.BaseDrawMain;

public class DrawBitmaps extends DrawOnFramesWithRangeValues {

	public static final int FRAMES_FOR_BITMAP = 2;
	public              int framesForBitmap   = DrawBitmaps.FRAMES_FOR_BITMAP;

	public Bitmap[] bitmaps;

	public int y;
	public int x;

	public DrawBitmaps(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public DrawBitmaps setFramesForBitmap(int framesForBitmap) {
		this.framesForBitmap = framesForBitmap;
		return this;
	}

	public DrawBitmaps setBitmaps(Bitmap[] bitmaps) {
		this.bitmaps = bitmaps;
		return this;
	}

	public DrawBitmaps setYX(int y, int x) {
		this.y = y;
		this.x = x;
		return this;
	}

	public DrawBitmaps animateRepeat(int repeat) {
		animateRange(0, bitmaps.length * framesForBitmap * repeat - 1);
		return this;
	}

	@Override
	public void draw(Canvas canvas, int value) {
		canvas.drawBitmap(bitmaps[value / framesForBitmap % bitmaps.length], x, y, null);
	}
}
