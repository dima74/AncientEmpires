package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.action.result.ActionResultGetUnit;

public class GameDrawRangeAll extends GameDraw
{
	
	private int	startI;
	private int	startJ;
	
	private boolean[][]	fieldMove;
	private boolean[][]	fieldAttack;
	private boolean[][]	fieldRaise;
	
	private int	radius;
	private int	diameter;
	
	private Bitmap[][] field;
	
	public void start(int startI, int startJ, ActionResultGetUnit result)
	{
		this.startI = startI;
		this.startJ = startJ;
		fieldMove = result.fieldMoveReal;
		fieldAttack = result.fieldAttackReal;
		fieldRaise = result.fieldRaiseReal;
		diameter = fieldMove.length;
		radius = diameter / 2;
		
		field = new Bitmap[diameter][diameter];
		for (int i = 0; i < diameter; i++)
			for (int j = 0; j < diameter; j++)
			{
				boolean isRedBitmap = fieldAttack[i][j] || fieldRaise[i][j];
				if (fieldMove[i][j] && isRedBitmap)
					field[i][j] = CursorImages().cursorMixed;// после замени катапульту на ведьму и добавь надгробие рядом с ней
				else if (fieldMove[i][j])
					field[i][j] = CursorImages().cursorWay;
				else if (isRedBitmap)
					field[i][j] = CursorImages().cursorAttack;
			}
	}
	
	public void end()
	{
		field = null;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (field != null)
			for (int relativeI = 0; relativeI < diameter; relativeI++)
				for (int relativeJ = 0; relativeJ < diameter; relativeJ++)
				{
					int i = startI - radius + relativeI;
					int j = startJ - radius + relativeJ;
					if (game.checkCoordinates(i, j) && field[relativeI][relativeJ] != null)
						canvas.drawBitmap(field[relativeI][relativeJ], j * A, i * A, null);
				}
	}
	
}