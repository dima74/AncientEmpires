package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

public class RenderScriptImages1
{
	
	public Bitmap			mBitmapIn;
	public Bitmap			mBitmapOut;
	private RenderScript	mRS;
	private Allocation		mInAllocation;
	private Allocation		mOutAllocation;
	private ScriptC_mono	mScript;
	
	public void getMatrixDataImage(Context context, ZipFile imagesZipFile, String zipPath) throws IOException
	{
		this.mBitmapIn = BitmapHelper.getBitmap(imagesZipFile, zipPath);
		
		this.mRS = RenderScript.create(context);
		// создание Renderscript-контекста
		this.mInAllocation = Allocation.createFromBitmap(this.mRS, this.mBitmapIn,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		// выделение и инициализация общей памяти для dalvik и renderscript контекстов
		this.mOutAllocation = Allocation.createTyped(this.mRS, this.mInAllocation.getType());
		// this.mScript = new ScriptC_mono(this.mRS, context.getResources(), R.raw.mono);
		this.mScript = new ScriptC_mono(this.mRS);
		// создание и привязка renderscript к renderscript-контексту
		this.mScript.forEach_root(this.mInAllocation, this.mOutAllocation);
		// вызываем renderscript-функцию root c SMP параллелизмом в 2 потока
		this.mOutAllocation.copyTo(this.mBitmapOut);
	}
}
