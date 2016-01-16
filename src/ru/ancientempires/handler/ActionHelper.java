package ru.ancientempires.handler;

import java.util.ArrayList;

import ru.ancientempires.action.CheckerCell;
import ru.ancientempires.action.CheckerUnit;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class ActionHelper extends GameHandler
{
	
	public boolean canUnitAction(Unit unit)
	{
		return unit != null && !unit.isTurn && unit.player == game.currentPlayer
				&& (canUnitMove(unit)
						|| canUnitAttack(unit)
						|| canUnitRepair(unit)
						|| canUnitCapture(unit)
						|| canUnitRaise(unit));
	}
	
	public boolean canUnitMove(Unit unit)
	{
		return !unit.isMove && isEmptyCells(unit.i, unit.j, unit.type);
	}
	
	public boolean canUnitAttack(final Unit unit)
	{
		return forUnitsInRange(unit.i, unit.j, unit.type.attackRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return targetUnit.player.team != unit.player.team;
			}
		}) || forCellsInRange(unit.i, unit.j, unit.type.attackRange, new CheckerCell()
		{
			@Override
			public boolean check(Cell targetCell)
			{
				return unit.type.destroyingTypes[targetCell.type.ordinal] && !targetCell.isDestroying && targetCell.getTeam() != unit.player.team;
			}
		});
	}
	
	public boolean canUnitRepair(Unit unit)
	{
		Cell cell = game.fieldCells[unit.i][unit.j];
		return unit.type.repairTypes[cell.type.ordinal] && cell.isDestroying;
	}
	
	public boolean canUnitCapture(Unit unit)
	{
		Cell cell = game.fieldCells[unit.i][unit.j];
		return unit.type.captureTypes[cell.type.ordinal] && !cell.isDestroying && cell.getTeam() != unit.player.team;
	}
	
	private boolean canUnitRaise(Unit unit)
	{
		return forUnitsInRange(game.fieldUnitsDead, unit.i, unit.j, unit.type.raiseRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return true;
			}
		});
	}
	
	public boolean forUnitsInRange(int i, int j, RangeType rangeType, CheckerUnit checker)
	{
		return forUnitsInRange(game.fieldUnits, i, j, rangeType, checker);
	}
	
	public boolean forUnitsInRange(Unit[][] field, int i, int j, RangeType rangeType, CheckerUnit checker)
	{
		boolean[][] rangeField = rangeType.field;
		int size = rangeField.length;
		for (int relI = 0; relI < size; relI++)
			for (int relJ = 0; relJ < size; relJ++)
			{
				if (!rangeField[relI][relJ])
					continue;
				int targetI = i + relI - rangeType.radius;
				int targetJ = j + relJ - rangeType.radius;
				if (!game.checkCoordinates(targetI, targetJ))
					continue;
				Unit unit = field[targetI][targetJ];
				if (unit != null && checker.check(unit))
					return true;
			}
		return false;
	}
	
	public Unit[] getUnitsInRange(int i, int j, RangeType rangeType, CheckerUnit checker)
	{
		return getUnitsInRange(game.fieldUnits, i, j, rangeType, checker);
	}
	
	public Unit[] getUnitsInRange(Unit[][] field, int i, int j, RangeType rangeType, CheckerUnit checker)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		
		boolean[][] rangeField = rangeType.field;
		int size = rangeField.length;
		for (int relI = 0; relI < size; relI++)
			for (int relJ = 0; relJ < size; relJ++)
			{
				if (!rangeField[relI][relJ])
					continue;
				int targetI = i + relI - rangeType.radius;
				int targetJ = j + relJ - rangeType.radius;
				if (!game.checkCoordinates(targetI, targetJ))
					continue;
				Unit unit = field[targetI][targetJ];
				if (unit != null && checker.check(unit))
					units.add(unit);
			}
		return units.toArray(new Unit[0]);
	}
	
	public boolean forCellsInRange(int i, int j, RangeType rangeType, CheckerCell checker)
	{
		boolean[][] rangeField = rangeType.field;
		int size = rangeField.length;
		for (int relI = 0; relI < size; relI++)
			for (int relJ = 0; relJ < size; relJ++)
			{
				if (!rangeField[relI][relJ])
					continue;
				int targetI = i + relI - rangeType.radius;
				int targetJ = j + relJ - rangeType.radius;
				if (!game.checkCoordinates(targetI, targetJ))
					continue;
				Cell cell = game.fieldCells[targetI][targetJ];
				if (cell != null && checker.check(cell))
					return true;
			}
		return false;
	}
	
	/*
	public boolean isUnit(int i, int j)
	{
		return game.getUnit(i, j) != null;
	}
	*/
	
	public boolean isEmptyCells(int i, int j, UnitType type)
	{
		return true;
	}
	
	public void clearUnitState(Unit unit)
	{
		unit.isMove = false;
		unit.isTurn = false;
	}
	
	public Unit[] getUnitsChangedStateNearCell(final Player player, final int targetI, final int targetJ)
	{
		return getUnitsInRange(targetI, targetJ, RangeType.MAX_ATTACK, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				if (targetUnit.player == player && !targetUnit.isTurn && !canUnitAction(targetUnit))
				{
					targetUnit.setTurn();
					return true;
				}
				return false;
			}
		});
	}
	
	private boolean checkAccess(Unit unit, int targetI, int targetJ)
	{
		boolean[][] field = unit.type.attackRange.field;
		int radius = field.length / 2;
		int i = radius + targetI - unit.i;
		int j = radius + targetJ - unit.j;
		return i >= 0 && i < field.length && j >= 0 && j < field.length && field[i][j];
	}
	
	// Для вызывания из gameDraw
	
	public boolean canBuyOnCell(int i, int j)
	{
		return game.floatingUnit == null && !game.fieldCells[i][j].type.buyUnitsDefault.isEmpty() && game.fieldCells[i][j].player == game.currentPlayer;
	}
	
	public boolean isUnitActive(int i, int j)
	{
		Unit unit = game.fieldUnits[i][j];
		Unit floatingUnit = game.floatingUnit;
		return unit != null && !unit.isTurn && unit.player == game.currentPlayer
				&& (floatingUnit == null || game.fieldUnits[floatingUnit.i][floatingUnit.j] == unit);
	}
	
	public boolean canUnitRepair(int i, int j)
	{
		return isUnitActive(i, j) && canUnitRepair(game.fieldUnits[i][j]);
	}
	
	public boolean canUnitCapture(int i, int j)
	{
		return isUnitActive(i, j) && canUnitCapture(game.fieldUnits[i][j]);
	}
	
}
