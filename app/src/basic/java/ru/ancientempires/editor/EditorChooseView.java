package ru.ancientempires.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import ru.ancientempires.MyColor;
import ru.ancientempires.activities.EditorActivity;
import ru.ancientempires.images.Images;

public class EditorChooseView extends View implements Callback {

	public static float mScale = 2.0f;
	public static int   mA     = (int) (Images.get().bitmapSize * mScale);
	public static int   A      = Images.get().bitmapSize;

	private float h;
	private float w;

	private EditorActivity activity;
	private MyColor[]      myColors;
	private DrawChoose     choose;
	private int            selectedStart;
	private EditorStruct[] structs;

	private float hDivider     = 2 / mScale;
	private int   xDivider     = 10;
	private Paint paintDivider = new Paint();

	public EditorChooseView(Context context, EditorActivity activity, EditorStruct[] structs, MyColor[] myColors, int selected) {
		super(context);
		this.activity = activity;
		this.myColors = myColors;
		this.structs = structs;
		selectedStart = selected;
		if (myColors.length > 0)
			for (EditorStruct struct : structs)
				struct.setColor(myColors[selectedStart]);
		paintDivider.setColor(Color.GRAY);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.h = h / mScale;
		this.w = w / mScale;

		if (myColors.length > 0) {
			choose = new DrawChoose(activity.getDrawMain());
			EditorStruct[] colors = new EditorStructColor[myColors.length];
			for (int i = 0; i < myColors.length; i++)
				colors[i] = new EditorStructColor(A, myColors[i]);
			choose.w = w;
			choose.selected = selectedStart;
			choose.create(colors, this);
		}

		// координаты верхнего левого угла превого квадратика
		int yFirst = A + (choose == null ? 0 : (int) (choose.h / mScale + hDivider));
		int xFirst = A;
		// между квадратиками
		int hBetween = A / 2;
		int wBetween = A / 2;
		int structsPerLine = (int) ((this.w - xFirst * 2 + wBetween) / (A + wBetween));
		for (int i = 0; i < structs.length; i++) {
			structs[i].y = yFirst + i / structsPerLine * (A + hBetween);
			structs[i].x = xFirst + A / 2 + i % structsPerLine * (A + wBetween);
		}

		// для postInvalidate()
		activity.getThread().view = this;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (choose != null && choose.touch(event.getY(), event.getX()))
			return true;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int i = EditorStruct.getNearest(structs, event.getY() / mScale, event.getX() / mScale);
			activity.getDrawMain().inputMain.setStruct(i, choose == null ? 0 : choose.selected);
			activity.dialog.dismiss();
			activity.dialog = null;
			return true;
		}
		return false;
	}

	@Override
	public void tapChoose(int i) {
		MyColor color = myColors[i];
		for (EditorStruct struct : structs)
			struct.setColor(color);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);

		if (choose != null) {
			choose.draw(canvas);
			float yDivider = choose.h;
			canvas.drawRect(xDivider, yDivider, w * mScale - xDivider, yDivider + hDivider, paintDivider);
		}

		canvas.scale(mScale, mScale);
		for (EditorStruct struct : structs)
			struct.drawBitmap(canvas);
	}
}
