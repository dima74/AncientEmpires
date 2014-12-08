package ru.ancientempires.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Short4;
import android.renderscript.Type;

public class RenderScriptCellImages
{
	private int					height;
	private int					width;
	private Config				bitmapConfig;
	private Type				allocationType;
	
	private RenderScript		mRS;
	private ScriptC_association	mScript;
	
	public void createScript(Context context)
	{
		// Initialize RS
		this.mRS = RenderScript.create(context);
		this.mScript = new ScriptC_association(this.mRS);
	}
	
	public void setBitmaps(Bitmap mBitmapInR, Bitmap mBitmapInG, Bitmap mBitmapInB)
	{
		// Allocate buffers
		Allocation mInAllocationR = Allocation.createFromBitmap(this.mRS, mBitmapInR);
		Allocation mInAllocationG = Allocation.createFromBitmap(this.mRS, mBitmapInG);
		Allocation mInAllocationB = Allocation.createFromBitmap(this.mRS, mBitmapInB);
		
		// Set global variable in RS
		this.mScript.set_red(mInAllocationR);
		this.mScript.set_green(mInAllocationG);
		this.mScript.set_blue(mInAllocationB);
		
		this.height = mBitmapInR.getHeight();
		this.width = mBitmapInR.getWidth();
		this.bitmapConfig = mBitmapInR.getConfig();
		this.allocationType = mInAllocationR.getType();
	}
	
	public Bitmap getBitmap(int color)
	{
		Bitmap mBitmapOut = Bitmap.createBitmap(this.width, this.height, this.bitmapConfig);
		
		Allocation mOutAllocation;
		// Allocate buffers
		mOutAllocation = Allocation.createTyped(this.mRS, this.allocationType);
		
		// Set global variable in RS
		Short4 colorShort4 = new Short4();
		colorShort4.w = (short) (color >> 8 * 3 & 0xFF);
		colorShort4.x = (short) (color >> 8 * 2 & 0xFF);
		colorShort4.y = (short) (color >> 8 * 1 & 0xFF);
		colorShort4.z = (short) (color >> 8 * 0 & 0xFF);
		this.mScript.set_color(colorShort4);
		
		this.mScript.forEach_association(mOutAllocation);
		
		// Copy to bitmap and invalidate image view
		mOutAllocation.copyTo(mBitmapOut);
		
		return mBitmapOut;
	}
}
