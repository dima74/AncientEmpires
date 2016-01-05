package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Unit;

public class ActionHandlerGetUnitWay extends ActionHandler
{
	
	public ActionHandlerGetUnitWay(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerGetUnitWay(this.type);
	}
	
	@Override
	public ActionResult action(final Action action)
	{
		final int i = (int) action.getProperty("i");
		final int j = (int) action.getProperty("j");
		
		createWay(i, j);
		
		return this.result;
	}
	
	private static final int[]	addI	=
	{
			-1,
			0,
			0,
			+1
	};
	private static final int[]	addJ	=
	{
			0,
			-1,
			+1,
			0
	};
	
	public void createWay(final int startI, final int startJ)
	{
		final Unit unit = GameHandler.fieldUnits[startI][startJ];
		final int maxLength = unit.type.moveRadius;
		
		final int size = maxLength * 2 + 1;
		final int[][] field = new int[size][size];
		final boolean[][] is = new boolean[size][size];
		final boolean[][] realIs = new boolean[size][size];
		final int[][] prevI = new int[size][size];
		final int[][] prevJ = new int[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				field[i][j] = maxLength + 1;
		field[maxLength][maxLength] = 0;
		prevI[maxLength][maxLength] = -1;
		prevJ[maxLength][maxLength] = -1;
		
		while (true)
		{
			int min = maxLength + 1;
			int minI = -1, minJ = -1;
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
					if (!is[i][j] && field[i][j] < min)
					{
						min = field[i][j];
						minI = i;
						minJ = j;
					}
			if (minI == -1)
				break;
			is[minI][minJ] = true;
			for (int k = 0; k < 4; k++)
			{
				final int nextI = minI + ActionHandlerGetUnitWay.addI[k];
				final int nextJ = minJ + ActionHandlerGetUnitWay.addJ[k];
				if (nextI < 0 || nextI >= size || nextJ < 0 || nextJ >= size)
					continue;
				final int nextICoord = startI + nextI - maxLength;
				final int nextJCoord = startJ + nextJ - maxLength;
				
				if (!GameHandler.checkCoord(nextICoord, nextJCoord))
					continue;
				Unit nextUnit = GameHandler.fieldUnits[nextICoord][nextJCoord];
				if (nextUnit != null && nextUnit.player != unit.player)
					continue;
				final int cellWeight = ActionHandlerGetUnitWay.getDifficulte(unit, nextICoord, nextJCoord);
				final int nextWeight = field[minI][minJ] + cellWeight;
				if (nextWeight < field[nextI][nextJ])
				{
					field[nextI][nextJ] = nextWeight;
					prevI[nextI][nextJ] = minI;
					prevJ[nextI][nextJ] = minJ;
					realIs[nextI][nextJ] = nextUnit == null || nextUnit.player != unit.player;
				}
			}
		}
		is[maxLength][maxLength] = false;
		
		this.result.setProperty("visibleField", is);
		this.result.setProperty("realField", realIs);
		this.result.setProperty("prevI", prevI);
		this.result.setProperty("prevJ", prevJ);
	}
	
	public static int getDifficulte(final Unit unit, final int nextICoord, final int nextJCoord)
	{
		if (unit.type.isFly)
			return 1;
		final Cell cell = GameHandler.fieldCells[nextICoord][nextJCoord];
		int add = 0;
		for (BonusOnCellGroup bonusOnCell : unit.type.bonusOnCellWay)
			if (cell.type.group == bonusOnCell.group)
				add += bonusOnCell.value;
		return cell.getSteps() + add;
	}
	
}
