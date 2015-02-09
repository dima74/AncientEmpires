#pragma version(1)
#pragma rs java_package_name(ru.ancientempires.images)
#pragma rs_fp_relaxed

rs_allocation red;
rs_allocation green;
rs_allocation blue;

int ca;
int cr;
int cg;
int cb;


int cr1;
int cg1;
int cb1;

int cr2;
int cg2;
int cb2;

int cr3;
int cg3;
int cb3;

int cr4;
int cg4;
int cb4;

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
	
	uchar4 out;
	out.a = ca;
	
	if (r.r == 0xF4)
	{
		cr = -155;
		cg = -6;
		cb = 259;
		out.r = max(0, (cr1 * r.r + cg1 * g.r + cb1 * b.r) >> 8);
		out.g = max(0, (cr1 * r.g + cg1 * g.g + cb1 * b.g) >> 8);
		out.b = max(0, (cr1 * r.b + cg1 * g.b + cb1 * b.b) >> 8);
	}
	else if (r.r == 0xDB)
	{
		cr = -34;
		cg = -20;
		cb = 146;
		out.r = max(0, (cr2 * r.r + cg2 * g.r + cb2 * b.r) >> 8);
		out.g = max(0, (cr2 * r.g + cg2 * g.g + cb2 * b.g) >> 8);
		out.b = max(0, (cr2 * r.b + cg2 * g.b + cb2 * b.b) >> 8);
	}
	else if (r.r == 0xA1)
	{
		cr = 0;
		cg = 10;
		cb = 95;
		out.r = max(0, (cr3 * r.r + cg3 * g.r + cb3 * b.r) >> 8);
		out.g = max(0, (cr3 * r.g + cg3 * g.g + cb3 * b.g) >> 8);
		out.b = max(0, (cr3 * r.b + cg3 * g.b + cb3 * b.b) >> 8);
	}
	else if (r.r == 0x5F)
	{
		cr = -19;
		cg = -27;
		cb = 152;
		out.r = max(0, (cr4 * r.r + cg4 * g.r + cb4 * b.r) >> 8);
		out.g = max(0, (cr4 * r.g + cg4 * g.g + cb4 * b.g) >> 8);
		out.b = max(0, (cr4 * r.b + cg4 * g.b + cb4 * b.b) >> 8);	
	}
	else
	{
		rsDebug("Fail", x, y);
		out.r = max(0, (cr * r.r + cg * g.r + cb * b.r) >> 8);
		out.g = max(0, (cr * r.g + cg * g.g + cb * b.g) >> 8);
		out.b = max(0, (cr * r.b + cg * g.b + cb * b.b) >> 8);
	}
	
	return out;
}
