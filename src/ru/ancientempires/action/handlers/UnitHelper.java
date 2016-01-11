package ru.ancientempires.action.handlers;

import java.util.HashMap;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.BonusForUnit;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.tasks.TaskIncreaseUnitAttack;
import ru.ancientempires.tasks.TaskIncreaseUnitDefence;
import ru.ancientempires.tasks.TaskRemoveTombstone;

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
				new TaskIncreaseUnitAttack()
						.setUnit(targetUnit)
						.setValue(-unit.type.bonusForUnitAfterAttackAttack)
						.setTurn(GameHandler.amountPlayers)
						.register();
			}
			if (unit.type.bonusForUnitAfterAttackDefence != 0)
			{
				targetUnit.defence += unit.type.bonusForUnitAfterAttackDefence;
				new TaskIncreaseUnitDefence()
						.setUnit(targetUnit)
						.setValue(-unit.type.bonusForUnitAfterAttackDefence)
						.setTurn(GameHandler.amountPlayers)
						.register();
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
			GameHandler.game.unitsStaticDead[unit.player.ordinal].add(unit);
		}
		if (unit.type.hasTombstone)
		{
			new TaskRemoveTombstone()
					.setIJ(unit.i, unit.j)
					.setTurn(GameHandler.amountPlayers + 1)
					.register();
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
