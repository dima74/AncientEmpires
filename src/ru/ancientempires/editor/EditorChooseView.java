package ru.ancientempires.editor;

import ru.ancientempires.MyColor;
import ru.ancientempires.draws.DrawInfo;
import ru.ancientempires.images.Images;

public class EditorChooseView extends View implements Callback
{
	
	public static float		mScale			= 2.0f;
	public static int		mA				= (int) (Images.get().bitmapSize * DrawInfo.mScale);
	public static int		A				= Images.get().bitmapSize;
											
	private float			h;
	private float			w;
							
	private MyColor[]		myColors;
	private DrawChoose		choose;
	private EditorStruct[]	structs;
							
	private float			hDivider		= 2 / mScale;
	// private int yDivider = choose.h;
	private int				xDivider		= 10;
	private Paint			paintDivider	= new Paint();
											
	public EditorChooseView(Context context, EditorStruct[] structs, MyColor[] myColors)
	{
		super(context);
		this.myColors = myColors;
		this.structs = structs;
		paintDivider.setColor(Color.GRAY);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		this.h = h / mScale;
		this.w = w / mScale;
		
		if (myColors.length > 0)
		{
			choose = new DrawChoose();
			EditorStruct[] colors = new EditorStructColor[myColors.length];
			for (int i = 0; i < myColors.length; i++)
				colors[i] = new EditorStructColor(A, myColors[i]);
			choose.w = w;
			choose.create(colors, this);
		}
		
		// координаты верхнего левого угла превого квадратика
		int yFirst = A + (choose == null ? 0 : (int) (choose.h / mScale + hDivider));
		int xFirst = A;
		// между квадратиками
		int hBetween = A / 2;
		int wBetween = A / 2;
		int structsPerLine = (int) ((this.w - xFirst * 2 + wBetween) / (A + wBetween));
		for (int i = 0; i < structs.length; i++)
		{
			structs[i].y = yFirst + i / structsPerLine * (A + hBetween);
			structs[i].x = xFirst + A / 2 + i % structsPerLine * (A + wBetween);
		}
		
		EditorThread.thread.view = this;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (choose != null && choose.touch(event.getY(), event.getX()))
			return true;
		EditorDrawMain.main.inputMain.update();
		return true;
	}
	
	@Override
	@Override
	@Override
	public void tapChoose(int i)
	{
		MyColor color = myColors[i];
		for (EditorStruct struct : structs)
			struct.setColor(color);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.WHITE);
		
		if (choose != null)
		{
			choose.draw(canvas);
			float yDivider = choose.h;
			canvas.drawRect(xDivider, yDivider, w * mScale - xDivider, yDivider + hDivider, paintDivider);
		}
		
		canvas.scale(mScale, mScale);
		for (int i = 0; i < structs.length; i++)
			structs[i].drawBitmap(canvas);
	}
	
}
