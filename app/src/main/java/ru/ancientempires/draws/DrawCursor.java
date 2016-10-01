package ru.ancientempires.draws;

import android.graphics.Canvas;

import ru.ancientempires.images.bitmaps.FewBitmaps;

public class DrawCursor extends Draw {

	private FewBitmaps cursor;
	private int        cursorH;
	private int        cursorW;

	public int cursorI;
	public int cursorJ;

	public DrawCursor(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public DrawCursor setCursor(FewBitmaps cursor) {
		this.cursor = cursor;
		cursorH = cursor.getBitmap().getHeight();
		cursorW = cursor.getBitmap().getWidth();
		return this;
	}

	public void tap(int i, int j) {
		cursorI = i;
		cursorJ = j;
	}

	@Override
	public void draw(Canvas canvas) {
		int cursorY = cursorI * A + (A - cursorH) / 2;
		int cursorX = cursorJ * A + (A - cursorW) / 2;
		canvas.drawBitmap(cursor.getBitmap(), cursorX, cursorY, null);
	}

}
