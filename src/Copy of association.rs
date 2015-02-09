#pragma version(1)
#pragma rs java_package_name(ru.ancientempires.images)
#pragma rs_fp_relaxed

rs_allocation red;
rs_allocation green;
rs_allocation blue;

uchar4 color;
float ca, cr, cg, cb;
float sum;

uchar4 __attribute__((kernel)) association(uint32_t x, uint32_t y)
{
	uchar4 r = rsGetElementAt_uchar4(red, x, y);
	uchar4 g = rsGetElementAt_uchar4(green, x, y);
	uchar4 b = rsGetElementAt_uchar4(blue, x, y);
	
	float4 r4 = rsUnpackColor8888(r);
	float4 g4 = rsUnpackColor8888(g);
	float4 b4 = rsUnpackColor8888(b);
	
	float ra = r4.a;
	float rr = r4.r;
	float rg = r4.g;
	float rb = r4.b;
	
	float ga = g4.a;
	float gr = g4.r;
	float gg = g4.g;
	float gb = g4.b;
	
	float ba = b4.a;
	float br = b4.r;
	float bg = b4.g;
	float bb = b4.b;
	
	if (ra == ga && ra == ba &&
		ra == gr && ra == br &&
		ra == gg && ra == bg &&
		ra == gb && ra == bb)
		return r;
	
	float4 f4;
	f4.a = ca;
	f4.r = (cr * rr + cg * gr + cb * br) / 0x100;
	f4.g = (cr * rg + cg * gg + cb * bg) / 0x100;
	f4.b = (cr * rb + cg * gb + cb * bb) / 0x100;
	
	return rsPackColorTo8888(f4);
}
