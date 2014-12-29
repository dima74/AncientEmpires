package ru.ancientempires.activity;

import ru.ancientempires.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Handler;
import android.view.View;

public class TestView extends View
{
	
	Bitmap					source;
	Bitmap					newBitmap;
	private BitmapShader	shader;
	private Paint			paint;
	private float			radius;
	
	Bitmap					bitmap;
	private long			st;
	
	public TestView(Context context)
	{
		super(context);
		
		this.source = BitmapFactory.decodeResource(getResources(), R.drawable.ii);
		// this.newBitmap = Bitmap.createBitmap(300, 300, Config.ARGB_8888);
		
		int size = Math.min(this.source.getWidth(), this.source.getHeight());
		// this.newBitmap = Bitmap.createBitmap(size, size, this.source.getConfig());
		
		this.paint = new Paint();
		this.shader = new BitmapShader(this.source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		this.paint.setShader(this.shader);
		
		this.radius = size / 2f;
		// canvas.drawCircle(radius, radius, radius, paint);
		
		this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my);
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
		
		this.st = System.currentTimeMillis();
		
		final Handler handler = new Handler();
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				invalidate();
				handler.postDelayed(this, 10);
			}
		};
		runnable.run();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		int a;
		Paint paint;
		a = 50;
		canvas.drawRect(a, a, getWidth() - a, getHeight() - a, TestViewGroup.getPaint(Color.CYAN));
		
		// a = 100;
		// paint = TestViewGroup.getPaint(Color.CYAN);
		// paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		// canvas.drawRect(a, a, getWidth() - a, getHeight() - a, paint);
		
		Bitmap bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics(),
				getWidth(), getHeight(), Config.ARGB_8888);
		bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
		
		Canvas top = new Canvas(bitmap);
		
		// canvas.save();
		
		a = 120;
		paint = TestViewGroup.getPaint(Color.BLUE);
		// top.drawRect(a, a, getWidth() - a, getHeight() - a, paint);
		top.drawCircle(a, a, (System.currentTimeMillis() - this.st) / 10, paint);
		
		paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		top.drawBitmap(this.bitmap, 100, 100, paint);
		
		canvas.drawBitmap(bitmap, 0, 0, null);
		// canvas.concat(top.getMatrix());
		
		// canvas.restore();
		// */
		
		/*
		Paint cyan = new Paint();
		cyan.setColor(Color.CYAN);
		int right = 205;
		int bottom = 205;
		canvas.drawRect(100, 100, right, bottom, cyan);
		
		canvas.save(Canvas.CLIP_SAVE_FLAG);
		Path path = new Path();
		path.addCircle(150, 150, 10, Direction.CW);
		canvas.clipPath(path);
		
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		canvas.drawRect(100, 100, right, bottom, blue);
		
		canvas.restore();
		
		Paint red = new Paint();
		red.setColor(Color.RED);
		canvas.drawRect(150, 150, getWidth() - 150, getHeight() - 150, red);
		*/
	}
}
