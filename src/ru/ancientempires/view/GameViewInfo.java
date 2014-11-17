package ru.ancientempires.view;

import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.model.Cell;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class GameViewInfo extends GameViewPart
{
	
	public GameViewInfo(Context context, GameView gameView)
	{
		super(context, gameView);
	}
	
	private static final Paint	paint	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color1	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color2	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color3	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color4	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color5	= new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private Bitmap				backgroundBitmap;
	
	private int					a;
	private int					h;
	private int					w;
	private float				mW;
	private int					color;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		this.backgroundBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(this.backgroundBitmap);
		
		GameViewInfo.color1.setColor(0xFFAFB7AB);
		GameViewInfo.color2.setColor(0xFF6D7581);
		GameViewInfo.color3.setColor(0xFF434A64);
		GameViewInfo.color4.setColor(0xFF242A45);
		GameViewInfo.color5.setColor(0xFF12142F);
		
		this.a = GameView.baseH / 24;
		this.h = h;
		this.w = w;
		this.mW = w - this.a * 7 - GameView.baseW;
		
		GameViewInfo.paint.setStrokeWidth(1);
		GameViewInfo.color1.setStrokeWidth(this.a);
		GameViewInfo.color2.setStrokeWidth(this.a);
		GameViewInfo.color3.setStrokeWidth(this.a);
		GameViewInfo.color4.setStrokeWidth(this.a);
		GameViewInfo.color5.setStrokeWidth(this.a);
		
		drawLeftPart(canvas, h, this.mW);
		drawRightPart(canvas, h, w, this.mW - this.a);
	}
	
	private void drawLeftPart(Canvas canvas, float h, float w)
	{
		float nh = h / this.a;
		float nw = w / this.a;
		canvas.drawRect(0, 0, w, h, GameViewInfo.color3);
		canvas.drawRect(this.a, this.a, w - this.a, h - this.a, GameViewInfo.color1);
		canvas.drawRect(3 * this.a, 3 * this.a, w - 3 * this.a, h - 3 * this.a, GameViewInfo.color3);
		
		drawLine(canvas, 3, 3.5f, 6, 3.5f, GameViewInfo.color5, true, true, true, nh, nw);
		drawLine(canvas, 4, 4.5f, 6, 4.5f, GameViewInfo.color4, false, true, true, nh, nw);
		drawLine(canvas, 4, 5.5f, 6, 5.5f, GameViewInfo.color4, false, true, true, nh, nw);
		drawLine(canvas, 3, 6.5f, 7, 6.5f, GameViewInfo.color1, true, true, true, nh, nw);
		drawLine(canvas, 7.5f, 4, 7.5f, 8, GameViewInfo.color2, false, false, true, nh, nw);
		drawLine(canvas, 3.5f, 8, 3.5f, nw - 8, GameViewInfo.color2, false, false, false, nh, nw);
		drawLine(canvas, 8, 4.5f, nh - 8, 4.5f, GameViewInfo.color5, false, false, true, nh, nw);
		drawLine(canvas, 4.5f, 8, 4.5f, nw - 8, GameViewInfo.color5, false, true, false, nh, nw);
		
		canvas.drawRect(5 * this.a, 8 * this.a, w - 5 * this.a, h - 8 * this.a, GameViewInfo.color4);
		canvas.drawRect(8 * this.a, 5 * this.a, w - 8 * this.a, h - 5 * this.a, GameViewInfo.color4);
	}
	
	public GameViewInfo updateGradient(int color)
	{
		this.color = color;
		invalidate();
		return this;
	}
	
	private void drawLine(Canvas canvas, float i1, float j1, float i2, float j2, Paint paint,
			boolean axial, boolean vertical, boolean horizontal, float nh, float nw)
	{
		canvas.drawLine(this.a * j1, this.a * i1, this.a * j2, this.a * i2, paint);
		if (axial)
			drawLine(canvas, j1, i1, j2, i2, paint, false, vertical, horizontal, nh, nw);
		if (vertical)
			drawLine(canvas, nh - i1, j1, nh - i2, j2, paint, false, false, horizontal, nh, nw);
		if (horizontal)
			drawLine(canvas, i1, nw - j1, i2, nw - j2, paint, false, false, false, nh, nw);
	}
	
	private void drawRightPart(Canvas canvas, float h, float w, float mW)
	{
		canvas.drawRect(mW, 0, w, h, GameViewInfo.color3);
		canvas.drawRect(mW + this.a, this.a, w - this.a, h - this.a, GameViewInfo.color1);
		float a3 = this.a * 3;
		canvas.drawRect(mW + a3, a3, w - a3, h - a3, GameViewInfo.color3);
	}
	
	private Cell[][]	field;
	private Bitmap		bitmap;
	private int			defense;
	
	public GameViewInfo setField(Cell[][] field)
	{
		this.field = field;
		return this;
	}
	
	@Override
	public boolean update()
	{
		final Cell cell = this.field[this.gameView.lastTapI][this.gameView.lastTapJ];
		this.bitmap = CellImages.getCellBitmap(cell, false);
		this.defense = cell.type.defense;
		invalidate();
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		MyLog.log("GameViewInfo.onTouchEvent()");
		return performClick();
	}
	
	@Override
	public boolean performClick()
	{
		return super.performClick();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(this.backgroundBitmap, 0, 0, null);
		
		if (this.bitmap != null)
		{
			// cell bitmap
			canvas.drawBitmap(this.bitmap, this.mW + this.a * 3, this.a * 4, null);
			
			// cell defense
			int x = this.w - this.a * 4 - NumberImages.w;
			final int y = this.h - this.a * 4 - NumberImages.h;
			int defense = this.defense;
			while (defense > 0)
			{
				canvas.drawBitmap(NumberImages.getNumberBitmap(defense % 10), x, y, null);
				defense /= 10;
				x -= NumberImages.w;
			}
			canvas.drawBitmap(NumberImages.defenceBitmap, x, y, null);
		}
		
		// gradient
		int amn = 8;
		for (int i = 0; i < amn; i++)
		{
			GameViewInfo.paint.setColor(this.color & 0x00FFFFFF |
				(int) (0xFF * (amn - i) / (amn + 0.0)) << 24);
			
			float y1 = (5 + i) * this.a;
			float x1 = (i < 3 ? 8 : 5) * this.a;
			float y2 = (5 + i) * this.a;
			float x2 = this.mW - (i < 3 ? 8 : 5) * this.a - .5f;
			for (int k = 0; k < this.a; k++)
				canvas.drawLine(x1, y1 + k, x2, y2 + k, GameViewInfo.paint);
		}
	}
}
