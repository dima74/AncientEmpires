package ru.ancientempires.editor;

import android.graphics.Canvas;

import ru.ancientempires.MyColor;
import ru.ancientempires.Paints;

public class EditorStructColor extends EditorStruct {

	private int     A;
	private MyColor color;

	public EditorStructColor(int a, MyColor color) {
		A = a;
		this.color = color;
	}

	@Override
	public void setColor(MyColor color) {}

	@Override
	public void drawBitmap(Canvas canvas) {
		canvas.drawRect(x - A / 2, y - A / 2, x + A / 2, y + A / 2, Paints.MY_COLORS[color.ordinal()]);
	}

	@Override
	public EditorStruct createCopy() {
		return new EditorStructColor(A, color).setYX(y, x);
	}
}
