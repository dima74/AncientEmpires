#pragma version(1)
#pragma rs java_package_name(ru.ancientempires.images)
#pragma rs_fp_relaxed

rs_allocation red;
rs_allocation green;
rs_allocation blue;

int ca2;
int cr2;
int cg2;
int cb2;
int sum2;

float ca;
float cr;
float cg;
float cb;
float sum;

uchar4 __attribute__((kernel)) association(uint32_t x, uint32_t y)
{
	uchar4 r = rsGetElementAt_uchar4(red, x, y);
	uchar4 g = rsGetElementAt_uchar4(green, x, y);
	uchar4 b = rsGetElementAt_uchar4(blue, x, y);
	
	if (r.a == g.a && r.a == b.a &&
		r.r == g.r && r.r == b.r &&
		r.g == g.g && r.g == b.g &&
		r.b == g.b && r.b == b.b)
		return r;
	
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
		rr == gr && rr == br &&
		rg == gg && rg == bg &&
		rb == gb && rb == bb)
		return r;
	
	float4 f4;
	f4.a = ca;
	f4.r = max(0.0, (cr * rr + cg * gr + cb * br));
	f4.g = max(0.0, (cr * rg + cg * gg + cb * bg));
	f4.b = max(0.0, (cr * rb + cg * gb + cb * bb));
	
	return rsPackColorTo8888(f4);
}
