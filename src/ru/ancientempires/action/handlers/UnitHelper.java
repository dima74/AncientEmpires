package ru.ancientempires.action.handlers;

import java.util.HashMap;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.BonusForUnit;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.tasks.Task;
import ru.ancientempires.tasks.TaskType;
import ru.ancientempires.tasks.handlers.TaskHandler;

public abstract class UnitHelper extends GameHandler
{
	
	public static int getQualitySum(Unit unit)
	{
		return (int) (2 * unit.attack) + unit.defence;
	}
	
	public static final int getNextRankExperience(Unit unit)
	{
		return UnitHelper.getQualitySum(unit) * 100 * 2 / 3;
	}
	
	public static boolean checkLevelUp(Unit unit)
	{
		int nextLevelExperience = UnitHelper.getNextRankExperience(unit);
		if (unit.experience >= nextLevelExperience)
		{
			unit.experience -= nextLevelExperience;
			unit.level++;
			return true;
		}
		return false;
	}
	
	public static int handleAfterAttackEffect(Unit unit, Unit targetUnit)
	{
		int bonusSign = (int) Math.signum(unit.type.bonusForUnitAfterAttackAttack + unit.type.bonusForUnitAfterAttackDefence);
		
		if (bonusSign != 0)
		{
			if (unit.type.bonusForUnitAfterAttackAttack != 0)
			{
				targetUnit.attack += unit.type.bonusForUnitAfterAttackAttack;
				Task task = new Task(TaskType.TASK_INCREASE_UNIT_ATTACK, GameHandler.amountPlayers);
				task.setProperty("unit", targetUnit);
				task.setProperty("value", -unit.type.bonusForUnitAfterAttackAttack);
				TaskHandler.addNewTask(task);
			}
			if (unit.type.bonusForUnitAfterAttackDefence != 0)
			{
				targetUnit.defence += unit.type.bonusForUnitAfterAttackDefence;
				Task task = new Task(TaskType.TASK_INCREASE_UNIT_DEFENSE, GameHandler.amountPlayers);
				task.setProperty("unit", targetUnit);
				task.setProperty("value", -unit.type.bonusForUnitAfterAttackDefence);
				TaskHandler.addNewTask(task);
			}
		}
		
		return bonusSign;
	}
	
	public static void checkDied(Unit unit)
	{
		if (unit.health > 0)
			return;
		if (unit.type.isStatic)
		{
			ActionHandlerHelper.clearUnitState(unit);
			GameHandler.game.staticUnitsDead[unit.player.ordinal].add(unit);
		}
		if (unit.type.hasTombstone)
		{
			Task task = new Task(TaskType.TASK_REMOVE_TOMBSTONE, GameHandler.amountPlayers + 1);
			task.setProperty("i", unit.i);
			task.setProperty("j", unit.j);
			TaskHandler.addNewTask(task);
			GameHandler.fieldDeadUnits[unit.i][unit.j] = unit;
		}
		
		unit.player.units.remove(unit);
		GameHandler.fieldUnits[unit.i][unit.j] = null;
	}
	
	public static int getDecreaseHealth(Unit unit, Unit targetUnit)
	{
		int add = unit.level * 2;
		for (BonusForUnit bonus : unit.type.bonusForUnitAttack)
			if (targetUnit.type == bonus.type)
				add += bonus.value;
				
		float part = GameHandler.game.random.nextFloat() * 2 - 1;
		return (int) Math.min(unit.health / 100f * Math.max(unit.attack + unit.attackDelta * part + add - UnitHelper.getUnitDefence(targetUnit), 0), targetUnit.health);
	}
	
	public static float getUnitDefence(Unit unit)
	{
		int add = 0;
		final Cell cell = GameHandler.fieldCells[unit.i][unit.j];
		for (BonusOnCellGroup bonus : unit.type.bonusOnCellDefence)
			if (cell.type.group == bonus.group)
				add += bonus.value;
		return unit.type.defence + add + unit.level * 2;
	}
	
	public static UnitType getKingType(Player player)
	{
		HashMap<MyColor, UnitType> colorToKing = new HashMap<MyColor, UnitType>();
		colorToKing.put(MyColor.BLUE, UnitType.getType("KING_GALAMAR"));
		colorToKing.put(MyColor.GREEN, UnitType.getType("KING_VALADORN"));
		colorToKing.put(MyColor.RED, UnitType.getType("KING_DEMONLORD"));
		colorToKing.put(MyColor.BLACK, UnitType.getType("KING_SAETH"));
		return colorToKing.get(player.color);
	}
	
	public static Unit getKing(Player player)
	{
		return UnitHelper.getKing(player, UnitHelper.getKingType(player));
	}
	
	public static Unit getKing(Player player, UnitType kingType)
	{
		for (Unit unit : player.units)
			if (unit.type == kingType)
				return unit;
		return null;
	}
	
}
