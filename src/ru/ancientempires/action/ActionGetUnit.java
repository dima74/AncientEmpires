package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResultGetUnit;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionGetUnit extends ActionFrom
{
	
	private ActionResultGetUnit	result	= new ActionResultGetUnit();
	private Unit				unit;
	private int					diameter;
	private int					radius;
	
	@Override
	public boolean changesGame()
	{
		return false;
	}
	
	@Override
	public ActionResultGetUnit perform()
	{
		unit = game.fieldUnits[i][j];
		if (unit == null || unit.player != game.currentPlayer || unit.isTurn)
			return result;
			
		radius = 0;
		radius = Math.max(radius, unit.moveRadius);
		radius = Math.max(radius, unit.type.attackRange.radius);
		radius = Math.max(radius, unit.type.raiseRange.radius);
		diameter = radius * 2 + 1;
		
		if (!unit.isMove && unit.checkFloating())
			createWay();
		else
		{
			result.fieldMove = new boolean[diameter][diameter];
			result.fieldMoveReal = new boolean[diameter][diameter];
		}
		if (unit.checkFloating())
		{
			createAttack();
			createRaise();
		}
		else
		{
			result.fieldAttack = new boolean[diameter][diameter];
			result.fieldAttackReal = new boolean[diameter][diameter];
			result.fieldRaise = new boolean[diameter][diameter];
			result.fieldRaiseReal = new boolean[diameter][diameter];
		}
		
		return commit(result);
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
	
	public void createWay()
	{
		final int[][] distance = new int[diameter][diameter];
		final boolean[][] fieldMove = new boolean[diameter][diameter];
		final boolean[][] fieldMoveReal = new boolean[diameter][diameter];
		final int[][] previousMoveI = new int[diameter][diameter];
		final int[][] previousMoveJ = new int[diameter][diameter];
		for (int i = 0; i < diameter; i++)
			for (int j = 0; j < diameter; j++)
				distance[i][j] = radius + 1;
		distance[radius][radius] = 0;
		previousMoveI[radius][radius] = -1;
		previousMoveJ[radius][radius] = -1;
		
		while (true)
		{
			int minDistance = radius + 1;
			int minDistanceI = -1, minDistanceJ = -1;
			for (int i = 0; i < diameter; i++)
				for (int j = 0; j < diameter; j++)
					if (!fieldMove[i][j] && distance[i][j] < minDistance)
					{
						minDistance = distance[i][j];
						minDistanceI = i;
						minDistanceJ = j;
					}
			if (minDistanceI == -1)
				break;
				
			fieldMove[minDistanceI][minDistanceJ] = true;
			for (int k = 0; k < 4; k++)
			{
				final int nextI = minDistanceI + ActionGetUnit.addI[k];
				final int nextJ = minDistanceJ + ActionGetUnit.addJ[k];
				if (nextI < 0 || nextI >= diameter || nextJ < 0 || nextJ >= diameter)
					continue;
				final int nextIAbsolute = unit.i + nextI - radius;
				final int nextJAbsolute = unit.j + nextJ - radius;
				
				if (!game.checkCoordinates(nextIAbsolute, nextJAbsolute))
					continue;
				Unit nextUnit = game.fieldUnits[nextIAbsolute][nextJAbsolute];
				if (nextUnit != null && nextUnit.player.team != unit.player.team)
					continue;
				final int steps = game.getSteps(unit, nextIAbsolute, nextJAbsolute);
				final int nextDistance = distance[minDistanceI][minDistanceJ] + steps;
				if (nextDistance < distance[nextI][nextJ])
				{
					distance[nextI][nextJ] = nextDistance;
					previousMoveI[nextI][nextJ] = minDistanceI;
					previousMoveJ[nextI][nextJ] = minDistanceJ;
					fieldMoveReal[nextI][nextJ] = nextUnit == null;
				}
			}
		}
		fieldMove[radius][radius] = false;
		fieldMoveReal[radius][radius] = false;
		
		result.fieldMove = fieldMove;
		result.fieldMoveReal = fieldMoveReal;
		result.previousMoveI = previousMoveI;
		result.previousMoveJ = previousMoveJ;
		
		for (int i = 0; i < diameter; i++)
			for (int j = 0; j < diameter; j++)
				if (fieldMoveReal[i][j])
				{
					result.canMove = true;
					return;
				}
	}
	
	private void createAttack()
	{
		boolean[][] fieldAttack = new boolean[diameter][diameter];
		boolean[][] fieldAttackReal = new boolean[diameter][diameter];
		
		create(unit.type.attackRange, fieldAttack, fieldAttackReal, new CheckerCoordinates()
		{
			@Override
			public boolean check(int targetI, int targetJ)
			{
				Unit targetUnit = game.fieldUnits[targetI][targetJ];
				Cell targetCell = game.fieldCells[targetI][targetJ];
				return targetUnit != null && targetUnit.player.team != unit.player.team
						|| unit.type.destroyingTypes[targetCell.type.ordinal] && !targetCell.isDestroying && targetCell.getTeam() != unit.player.team;
			}
		});
		
		result.fieldAttack = fieldAttack;
		result.fieldAttackReal = fieldAttackReal;
		for (int i = 0; i < diameter; i++)
			for (int j = 0; j < diameter; j++)
				if (fieldAttackReal[i][j])
				{
					result.canAttack = true;
					return;
				}
	}
	
	private void createRaise()
	{
		boolean[][] fieldRaise = new boolean[diameter][diameter];
		boolean[][] fieldRaiseReal = new boolean[diameter][diameter];
		
		create(unit.type.raiseRange, fieldRaise, fieldRaiseReal, new CheckerCoordinates()
		{
			@Override
			public boolean check(int targetI, int targetJ)
			{
				return game.fieldUnitsDead[targetI][targetJ] != null;
			}
		});
		
		result.fieldRaise = fieldRaise;
		result.fieldRaiseReal = fieldRaiseReal;
		for (int i = 0; i < diameter; i++)
			for (int j = 0; j < diameter; j++)
				if (fieldRaise[i][j])
				{
					result.canRaise = true;
					return;
				}
	}
	
	private void create(RangeType range, boolean[][] field, boolean[][] fieldReal, CheckerCoordinates checker)
	{
		int startRelativeI = Math.max(-unit.i, -range.radius);
		int startRelativeJ = Math.max(-unit.j, -range.radius);
		int endRelativeI = Math.min(game.h - unit.i - 1, range.radius);
		int endRelativeJ = Math.min(game.w - unit.j - 1, range.radius);
		for (int relativeI = startRelativeI; relativeI <= endRelativeI; relativeI++)
			for (int relativeJ = startRelativeJ; relativeJ <= endRelativeJ; relativeJ++)
				if (range.field[range.radius + relativeI][range.radius + relativeJ])
				{
					field[radius + relativeI][radius + relativeJ] = true;
					int targetI = unit.i + relativeI;
					int targetJ = unit.j + relativeJ;
					fieldReal[radius + relativeI][radius + relativeJ] = checker.check(targetI, targetJ);
				}
	}
	
}
