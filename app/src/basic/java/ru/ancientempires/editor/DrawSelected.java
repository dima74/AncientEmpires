package ru.ancientempires.editor;

import android.graphics.Canvas;

import ru.ancientempires.Paints;
import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.draws.Draw;

public class DrawSelected extends Draw {

	public static int h = 2;
	public  int        y;
	public  int        x;        // координата центра полоски
	public  int        targetX;
	private DrawChoose choose;

	public DrawSelected(BaseDrawMain mainBase, int y, DrawChoose choose) {
		super(mainBase);
		this.y = y;
		this.choose = choose;
	}

	@Override
	public void draw(Canvas canvas) {
		x += (targetX - x) / 2;
		canvas.drawRect(x, y, x + choose.mA, y + h, Paints.RED);
	}
}