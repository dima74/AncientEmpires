package ru.ancientempires.images;

import ru.ancientempires.MyColor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.Type;

public class AssociationScript
{
	
	private int						height;
	private int						width;
	private Config					bitmapConfig;
	private Type					allocationType;
	
	private RenderScript			mRS;
	//private ScriptC_association		mScript;
	
	public static AssociationScript	rs;
	
	public AssociationScript(Context context)
	{
		// Initialize RS
		this.mRS = RenderScript.create(context);
		//this.mScript = new ScriptC_association(this.mRS);
	}
	
	public void setBitmaps(Bitmap mBitmapInR, Bitmap mBitmapInG, Bitmap mBitmapInB)
	{
		// Allocate buffers
		Allocation mInAllocationR = Allocation.createFromBitmap(this.mRS, mBitmapInR);
		Allocation mInAllocationG = Allocation.createFromBitmap(this.mRS, mBitmapInG);
		Allocation mInAllocationB = Allocation.createFromBitmap(this.mRS, mBitmapInB);
		
		// Set global variable in RS
		/*
		this.mScript.set_red(mInAllocationR);
		this.mScript.set_green(mInAllocationG);
		this.mScript.set_blue(mInAllocationB);
		*/
		
		this.height = mBitmapInR.getHeight();
		this.width = mBitmapInR.getWidth();
		this.bitmapConfig = mBitmapInR.getConfig();
		this.allocationType = mInAllocationR.getType();
	}
	
	public Bitmap getBitmap(MyColor color)
	{
		Bitmap mBitmapOut = Bitmap.createBitmap(this.width, this.height, this.bitmapConfig);
		
		Allocation mOutAllocation;
		// Allocate buffers
		mOutAllocation = Allocation.createTyped(this.mRS, this.allocationType);
		
		// Set global variable in RS
		/*
		this.mScript.set_ca(color.a);
		this.mScript.set_cr(color.r);
		this.mScript.set_cg(color.g);
		this.mScript.set_cb(color.b);
		
		this.mScript.set_cr1(color.r1);
		this.mScript.set_cg1(color.g1);
		this.mScript.set_cb1(color.b1);
		
		this.mScript.set_cr2(color.r2);
		this.mScript.set_cg2(color.g2);
		this.mScript.set_cb2(color.b2);
		
		this.mScript.set_cr3(color.r3);
		this.mScript.set_cg3(color.g3);
		this.mScript.set_cb3(color.b3);
		
		this.mScript.set_cr4(color.r4);
		this.mScript.set_cg4(color.g4);
		this.mScript.set_cb4(color.b4);
		
		this.mScript.forEach_association(mOutAllocation);
		*/
		
		// Copy to bitmap and invalidate image view
		mOutAllocation.copyTo(mBitmapOut);
		
		return mBitmapOut;
	}
	
}
