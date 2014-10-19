package ru.ancientempires;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.images.RenderScriptImages;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

public class MainActivity2 extends Activity
{
	
	public Bitmap				mBitmapIn;
	public Bitmap				mBitmapOut;
	
	public RenderScriptImages	rsi	= new RenderScriptImages();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(new MyView(getBaseContext()));
		
		ZipFile imagesZipFile;
		try
		{
			imagesZipFile = getZipFileFromAssets(getAssets(), "images.zip");
			this.mBitmapIn = BitmapHelper.getBitmap(imagesZipFile, "levelup.png");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.mBitmapOut = Bitmap.createBitmap(this.mBitmapIn.getWidth(), this.mBitmapIn.getHeight(),
				this.mBitmapIn.getConfig());
		this.rsi.mBitmapIn = this.mBitmapIn;
		this.rsi.mBitmapOut = this.mBitmapOut;
		
		this.rsi.createScript(this);
		
		// this.mBitmapOut = this.rsi.mBitmapOut;
	}
	
	public ZipFile getZipFileFromAssets(AssetManager assets, String name) throws IOException
	{
		InputStream inputStream = assets.open(name);
		File zipFileOutput = new File(getBaseContext().getExternalCacheDir(), name);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFileOutput);
		
		// TODO расширить, если файл > 1МБ
		byte[] buffer = new byte[1024 * 1024];
		int length = inputStream.read(buffer);
		fileOutputStream.write(buffer, 0, length);
		
		fileOutputStream.close();
		
		ZipFile zipFile = new ZipFile(new File(getBaseContext().getExternalCacheDir(), name));
		return zipFile;
	}
	
	public class MyView extends View
	{
		
		public MyView(Context context)
		{
			super(context);
			invalidate();
		}
		
		@Override
		protected void onDraw(Canvas canvas)
		{
			canvas.drawBitmap(MainActivity2.this.mBitmapIn, 100, 100, null);
			canvas.drawBitmap(MainActivity2.this.mBitmapOut, 100, 200, null);
		}
	}
	
}
