package ru.ancientempires.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptC;

public class RenderScriptImages
{
	
	public Bitmap			mBitmapIn;
	public Bitmap			mBitmapOut;
	
	private RenderScript	mRS;
	private Allocation		mInAllocation;
	private Allocation		mOutAllocation;
	private ScriptC			mScript;
	
	/*
	 * Initialize RenderScript
	 * In the sample, it creates RenderScript kernel that performs saturation manipulation.
	 */
	public void createScript(Context context)
	{
		// Initialize RS
		this.mRS = RenderScript.create(context);
		
		// Allocate buffers
		this.mInAllocation = Allocation.createFromBitmap(this.mRS, this.mBitmapIn);
		this.mOutAllocation = Allocation.createTyped(this.mRS, this.mInAllocation.getType());
		
		// Load script
		// this.mScript = new ScriptC(this.mRS);
		
		/*
		 * Set global variable in RS
		 */
		// this.mScript.set_saturationValue(-1.0f);
		
		/*
		 * Invoke saturation filter kernel
		 */
		// this.mScript.forEach_saturation(this.mInAllocation, this.mOutAllocation);
		
		/*
		 * Copy to bitmap and invalidate image view
		 */
		this.mOutAllocation.copyTo(this.mBitmapOut);
	}
	
}
