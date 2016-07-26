package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ru.ancientempires.actions.result.ActionResultGetUnit;

public class DrawRange extends Draw
{
	
	private int startI;
	private int startJ;
	
	private int radius;
	private int diameter;
	
	private Bitmap[][] field;
	
	public DrawRange(BaseDrawMain mainBase, int startI, int startJ, ActionResultGetUnit result)
	{
		super(mainBase);
		this.startI = startI;
		this.startJ = startJ;
		diameter = result.fieldMoveReal.length;
		radius = diameter / 2;
		
		field = new Bitmap[diameter][diameter];
		for (int i = 0; i < diameter; i++)
			for (int j = 0; j < diameter; j++)
			{
				boolean isRedBitmap = result.fieldAttackReal[i][j] || result.fieldRaiseReal[i][j];
				if (result.fieldMoveReal[i][j] && isRedBitmap)
					field[i][j] = CursorImages().cursorMixed;// после замени катапульту на ведьму и добавь надгробие рядом с ней
				else if (result.fieldMoveReal[i][j])
					field[i][j] = CursorImages().cursorWay;
				else if (isRedBitmap)
					field[i][j] = CursorImages().cursorAttack;
			}
	}
	
	@Override
	public void draw(Canvas canvas)
	{
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
