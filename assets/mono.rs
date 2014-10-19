#pragma version(1)
#pragma rs java_package_name(ru.ancientempires.images)
#pragma rs_fp_relaxed

//multipliers to convert a RGB colors to black and white
const static float3 gMonoMult = {0.299f, 0.587f, 0.114f};

void root(const uchar4 *v_in, uchar4 *v_out)
{
	//unpack a color to a float4
	float4 f4 = rsUnpackColor8888(*v_in);
	//take the dot product of the color and the multiplier
	float3 mono = dot(f4.rgb, gMonoMult);
	//repack the float to a color
	*v_out = rsPackColorTo8888(mono);
}