package ru.ancientempires.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MatrixHelper
{
	
	public static void writeBinaryMatrixFileToTXT(File binaryFile, int height, int width, File txtFile) throws IOException
	{
		FileInputStream input = new FileInputStream(binaryFile);
		PrintWriter output = new PrintWriter(txtFile);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int ordinal = 0;
				ordinal += input.read() << 3 * 8;
				ordinal += input.read() << 2 * 8;
				ordinal += input.read() << 1 * 8;
				ordinal += input.read() << 0 * 8;
				output.print(BeatifullHelper.toBeatifullString(ordinal + "", 3) + " ");
			}
			output.println();
		}
		
		input.close();
		output.close();
	}
	
	public static void writeTXTMatrixFileToBinary(File txtFile, File binaryFile) throws IOException
	{
		Scanner input = new Scanner(txtFile);
		FileOutputStream output = new FileOutputStream(binaryFile);
		
		int x;
		while (input.hasNextInt())
		{
			x = input.nextInt();
			
			output.write(x >> 3 * 8 & 0xFF);
			output.write(x >> 2 * 8 & 0xFF);
			output.write(x >> 1 * 8 & 0xFF);
			output.write(x >> 0 * 8 & 0xFF);
		}
		
		input.close();
		output.close();
	}
	
	public static int[] getArrayFromMatrix(int[][] matrix)
	{
		int height = matrix.length;
		int width = matrix[0].length;
		int[] array = new int[height * width];
		
		int k = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				array[k] = matrix[i][j];
				k++;
			}
		
		return array;
	}
	
	public static int[][] copyMatrix(int[][] baseMatrix, int startI, int startJ, int height, int width)
	{
		int[][] resultMatrix = new int[height][width];
		for (int i = startI; i < startI + height; i++)
			for (int j = startJ; j < startJ + width; j++)
				resultMatrix[i - startI][j - startJ] = baseMatrix[i][j];
		return resultMatrix;
	}
	
}
