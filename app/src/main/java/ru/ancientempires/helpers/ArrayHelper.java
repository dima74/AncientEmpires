package ru.ancientempires.helpers;

public class ArrayHelper
{
	
	public static String arrayToString(boolean[] arr)
	{
		String s = "";
		for (boolean element : arr)
			s += BeatifullHelper.toBeatifullString(element, 1) + " ";
		return s;
	}
	
	public static int[][] getMatrixFromArray(int[] array, int height, int width)
	{
		int[][] matrix = new int[height][width];
		
		int k = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				matrix[i][j] = array[k];
				k++;
			}
		
		return matrix;
	}
	
}
