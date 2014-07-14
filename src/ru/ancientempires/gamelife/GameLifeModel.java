package ru.ancientempires.gamelife;

import java.util.Random;

public class GameLifeModel
{
	
	private static final int	NEIGHBOURS_MIN	= 3;
	private static final int	NEIGHBOURS_MAX	= 5;
	private static final int	NEIGHBOURS_NEED	= 1;
	
	private Random				random;
	
	public int					rows;
	public int					columns;
	public int					cells;
	
	public byte[][]				gameField;
	
	public GameLifeModel(int rowsAmount, int columnsAmount, int cellsAmount)
	{
		this.rows = rowsAmount;
		this.columns = columnsAmount;
		this.cells = cellsAmount;
		
		this.gameField = new byte[this.columns][this.rows];
		
		initField();
	}
	
	private void initField()
	{
		this.random = new Random();
		int i, j;
		for (int k = 0; k < this.cells; k++)
		{
			do
			{
				i = this.random.nextInt(this.columns);
				j = this.random.nextInt(this.rows);
			}
			while (this.gameField[i][j] == 1);
			this.gameField[i][j] = 1;
		}
	}
	
	public String toString(byte[][] field)
	{
		String s = "";
		
		for (byte[] column : field)
		{
			for (byte cell : column)
			{
				s += cell;
			}
			s += "\n";
		}
		
		return s;
	}
	
	public void nextTurn()
	{
		byte[][] nextField = new byte[this.columns][this.rows];
		
		for (int i = 0; i < this.columns; i++)
		{
			for (int j = 0; j < this.rows; j++)
			{
				int amountNeighbours = 0;
				for (int ii = i - 1; ii < i + 1; ii++)
				{
					for (int ij = j - 1; ij < j + 1; ij++)
					{
						if (!(ii == i && ij == j))
						{
							amountNeighbours += isLive(ii, ij) ? 1 : 0;
						}
					}
				}
				if (amountNeighbours == NEIGHBOURS_NEED || this.random.nextInt(10) == 3)
				{
					nextField[i][j] = 1;
				}
				else if (amountNeighbours < NEIGHBOURS_MIN || amountNeighbours > NEIGHBOURS_MAX || this.random.nextInt(10) == 7)
				{
					nextField[i][j] = 0;
				}
			}
		}
		
		this.gameField = nextField;
	}
	
	public int getCount()
	{
		return this.columns * this.rows;
	}
	
	private boolean isLive(int i, int j)
	{
		if (i < 0 || i >= this.columns || j < 0 || j >= this.rows)
			return false;
		return this.gameField[i][j] == 1;
		
	}
	
	public boolean isLive(int position)
	{
		
		int i = position / this.rows;
		int j = position % this.rows;
		
		return isLive(i, j);
	}
	
}
