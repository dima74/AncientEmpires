package ru.ancientempires.helpers;

public class ImageHelper
{
	
	public static int[][] getNewImg(int[][] dataRed, int[][] dataGreen, int[][] dataBlue, int color)
	{
		assert dataRed.length == dataGreen.length;
		assert dataRed.length == dataBlue.length;
		assert dataRed[0].length == dataGreen[0].length;
		assert dataRed[0].length == dataBlue[0].length;
		int height = dataRed.length;
		int width = dataRed[0].length;
		
		int[][] data = new int[height][width];
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				int r = dataRed[i][j];
				int g = dataGreen[i][j];
				int b = dataBlue[i][j];
				
				if (r == g && r == b)
					data[i][j] = r;
				else
				{
					int cA = ImageHelper.getA(color);
					int cR = ImageHelper.getR(color);
					int cG = ImageHelper.getG(color);
					int cB = ImageHelper.getB(color);
					
					// int rA = ImageHelper.getA(r);
					int rR = ImageHelper.getR(r);
					int rG = ImageHelper.getG(r);
					int rB = ImageHelper.getB(r);
					
					// int gA = ImageHelper.getA(g);
					int gR = ImageHelper.getR(g);
					int gG = ImageHelper.getG(g);
					int gB = ImageHelper.getB(g);
					
					// int bA = ImageHelper.getA(b);
					int bR = ImageHelper.getR(b);
					int bG = ImageHelper.getG(b);
					int bB = ImageHelper.getB(b);
					
					// float mA = cA / (float) 0xFF;
					float mR = cR / (float) 0xFF;
					float mG = cG / (float) 0xFF;
					float mB = cB / (float) 0xFF;
					
					float mSum = mR + mG + mB;
					
					int ncA = cA;
					int ncR = (int) ((mR * rR + mG * gR + mB * bR) / mSum);
					int ncG = (int) ((mR * rG + mG * gG + mB * bG) / mSum);
					int ncB = (int) ((mR * rB + mG * gB + mB * bB) / mSum);
					
					data[i][j] = ImageHelper.getColor(ncA, ncR, ncG, ncB);
					System.out.print("");
				}
			}
		
		return data;
	}
	
	public static int getColor(int a, int r, int g, int b)
	{
		return (a << 3 * 8) + (r << 2 * 8) + (g << 1 * 8) + (b << 0 * 8);
	}
	
	public static int getA(int color)
	{
		// 0xAArrggbb
		return ImageHelper.get(color, 3);
	}
	
	public static int getR(int color)
	{
		// 0xaaRRggbb
		return ImageHelper.get(color, 2);
	}
	
	public static int getG(int color)
	{
		// 0xaarrGGbb
		return ImageHelper.get(color, 1);
	}
	
	public static int getB(int color)
	{
		// 0xaarrggBB
		return ImageHelper.get(color, 0);
	}
	
	private static int get(int color, int offset)
	{
		return color >>> offset * 8 & 0xFF;
	}
	
}
