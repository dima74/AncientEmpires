package ru.ancientempires.action.handlers;

import java.util.ArrayList;

import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class ActionHandlerHelper extends GameHandler
{
	
	public static void calculateCacheValues()
	{
		for (Cell[] line : GameHandler.fieldCells)
			for (Cell cell : line)
				if (cell.player != null)
					GameHandler.game.currentEarns[cell.player.ordinal] += cell.type.earn;
	}
	
	public static ArrayList<ActionType> getAvailableActionsForUnit(Unit unit)
	{
		ArrayList<ActionType> actionTypes = new ArrayList<ActionType>();
		if (unit != null && !unit.isTurn && unit.player == GameHandler.game.currentPlayer)
		{
			if (ActionHandlerHelper.canUnitMove(unit))
				actionTypes.add(ActionType.ACTION_UNIT_MOVE);
				
			if (ActionHandlerHelper.canUnitAttack(unit))
				actionTypes.add(ActionType.ACTION_UNIT_ATTACK);
				
			if (ActionHandlerHelper.canUnitRepair(unit))
				actionTypes.add(ActionType.ACTION_UNIT_REPAIR);
			else if (ActionHandlerHelper.canUnitCapture(unit))
				actionTypes.add(ActionType.ACTION_UNIT_CAPTURE);
				
			else if (ActionHandlerHelper.canUnitRaise(unit))
				actionTypes.add(ActionType.ACTION_UNIT_RAISE);
		}
		return actionTypes;
	}
	
	public static boolean canUnitMove(Unit unit)
	{
		return !unit.isMove && ActionHandlerHelper.isEmptyCells(unit.i, unit.j, unit.type);
	}
	
	public static boolean canUnitAttack(final Unit unit)
	{
		return ActionHandlerHelper.forUnitsInRange(unit.i, unit.j, unit.type.attackRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return targetUnit.player.team != unit.player.team;
			}
		}) || ActionHandlerHelper.forCellsInRange(unit.i, unit.j, unit.type.attackRange, new CheckerCell()
		{
			@Override
			public boolean check(Cell targetCell)
			{
				return unit.type.destroyingTypes[targetCell.type.ordinal] && !targetCell.isDestroying && targetCell.getTeam() != unit.player.team;
			}
		});
	}
	
	public static boolean canUnitRepair(Unit unit)
	{
		Cell cell = GameHandler.fieldCells[unit.i][unit.j];
		return unit.type.repairTypes[cell.type.ordinal] && cell.isDestroying;
	}
	
	public static boolean canUnitCapture(Unit unit)
	{
		Cell cell = GameHandler.fieldCells[unit.i][unit.j];
		return unit.type.captureTypes[cell.type.ordinal] && !cell.isDestroying && cell.getTeam() != unit.player.team;
	}
	
	private static boolean canUnitRaise(Unit unit)
	{
		return ActionHandlerHelper.forUnitsInRange(GameHandler.fieldDeadUnits, unit.i, unit.j, unit.type.raiseRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return true;
			}
		});
	}
	
	public static boolean forUnitsInRange(int i, int j, RangeType rangeType, CheckerUnit checker)
	{
		return ActionHandlerHelper.forUnitsInRange(GameHandler.fieldUnits, i, j, rangeType, checker);
	}
	
	public static boolean forUnitsInRange(Unit[][] field, int i, int j, RangeType rangeType, CheckerUnit checker)
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
				if (!GameHandler.checkCoord(targetI, targetJ))
					continue;
				Unit unit = field[targetI][targetJ];
				if (unit != null && checker.check(unit))
					return true;
			}
		return false;
	}
	
	public static Unit[] getUnitsInRange(int i, int j, RangeType rangeType, CheckerUnit checker)
	{
		return ActionHandlerHelper.getUnitsInRange(GameHandler.fieldUnits, i, j, rangeType, checker);
	}
	
	public static Unit[] getUnitsInRange(Unit[][] field, int i, int j, RangeType rangeType, CheckerUnit checker)
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
				if (!GameHandler.checkCoord(targetI, targetJ))
					continue;
				Unit unit = field[targetI][targetJ];
				if (unit != null && checker.check(unit))
					units.add(unit);
			}
		return units.toArray(new Unit[0]);
	}
	
	public static boolean forCellsInRange(int i, int j, RangeType rangeType, CheckerCell checker)
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
				if (!GameHandler.checkCoord(targetI, targetJ))
					continue;
				Cell cell = GameHandler.fieldCells[targetI][targetJ];
				if (cell != null && checker.check(cell))
					return true;
			}
		return false;
	}
	
	public static boolean isUnit(int i, int j)
	{
		return GameHandler.getUnit(i, j) != null;
	}
	
	public static boolean isEmptyCells(int i, int j, UnitType type)
	{
		return true;
	}
	
	public static void clearUnitState(Unit unit)
	{
		unit.isMove = false;
		unit.isTurn = false;
	}
	
	public static Unit[] getUnitsChangedStateNearCell(final Player player, final int targetI, final int targetJ)
	{
		return ActionHandlerHelper.getUnitsInRange(targetI, targetJ, RangeType.MAX_ATTACK, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				if (targetUnit.player == player && !targetUnit.isTurn && ActionHandlerHelper.getAvailableActionsForUnit(targetUnit).isEmpty())
				{
					targetUnit.setTurn();
					return true;
				}
				return false;
			}
		});
	}
	
	private static boolean checkAccess(Unit unit, int targetI, int targetJ)
	{
		boolean[][] field = unit.type.attackRange.field;
		int radius = field.length / 2;
		int i = radius + targetI - unit.i;
		int j = radius + targetJ - unit.j;
		return i >= 0 && i < field.length && j >= 0 && j < field.length && field[i][j];
	}
	
	// Для вызывания из gameDraw
	
	public static boolean canBuyOnCell(int i, int j)
	{
		return GameHandler.game.floatingUnit == null && !GameHandler.fieldCells[i][j].type.buyUnitsDefault.isEmpty() && GameHandler.fieldCells[i][j].player == GameHandler.game.currentPlayer;
	}
	
	public static boolean isActiveUnit(int i, int j)
	{
		Unit unit = GameHandler.fieldUnits[i][j];
		Unit floatingUnit = GameHandler.game.floatingUnit;
		return unit != null && !unit.isTurn && unit.player == GameHandler.game.currentPlayer
				&& (floatingUnit == null || GameHandler.fieldUnits[floatingUnit.i][floatingUnit.j] == unit);
	}
	
	public static boolean canUnitRepair(int i, int j)
	{
		return ActionHandlerHelper.isActiveUnit(i, j) && ActionHandlerHelper.canUnitRepair(GameHandler.fieldUnits[i][j]);
	}
	
	public static boolean canUnitCapture(int i, int j)
	{
		return ActionHandlerHelper.isActiveUnit(i, j) && ActionHandlerHelper.canUnitCapture(GameHandler.fieldUnits[i][j]);
	}
	
}
