package ru.ancientempires.handler;

import java.util.HashMap;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.tasks.TaskIncreaseUnitAttack;
import ru.ancientempires.tasks.TaskIncreaseUnitDefence;
import ru.ancientempires.tasks.TaskRemoveTombstone;

public class UnitHelper extends GameHandler
{
	
	public UnitHelper(Game game)
	{
		setGame(game);
	}
	
	public int getQualitySum(Unit unit)
	{
		return unit.type.attackMin + unit.type.attackMax + unit.type.defence;
	}
	
	public final int getNextRankExperience(Unit unit)
	{
		return getQualitySum(unit) * 100 * 2 / 3;
	}
	
	public boolean checkLevelUp(Unit unit)
	{
		int nextLevelExperience = getNextRankExperience(unit);
		if (unit.experience >= nextLevelExperience)
		{
			unit.experience -= nextLevelExperience;
			unit.level++;
			return true;
		}
		return false;
	}
	
	public int handleAfterAttackEffect(Unit unit, Unit targetUnit)
	{
		int bonusSign = (int) Math.signum(unit.type.bonusForUnitAfterAttackAttack + unit.type.bonusForUnitAfterAttackDefence);
		if (bonusSign != 0)
		{
			if (unit.type.bonusForUnitAfterAttackAttack != 0)
			{
				targetUnit.attack += unit.type.bonusForUnitAfterAttackAttack;
				new TaskIncreaseUnitAttack(game)
						.setUnit(targetUnit)
						.setValue(-unit.type.bonusForUnitAfterAttackAttack)
						.setTurn(game.numberPlayers())
						.register();
			}
			if (unit.type.bonusForUnitAfterAttackDefence != 0)
			{
				targetUnit.defence += unit.type.bonusForUnitAfterAttackDefence;
				new TaskIncreaseUnitDefence(game)
						.setUnit(targetUnit)
						.setValue(-unit.type.bonusForUnitAfterAttackDefence)
						.setTurn(game.numberPlayers())
						.register();
			}
		}
		return bonusSign;
	}
	
	public void checkDied(Unit unit)
	{
		if (unit.health > 0)
			return;
		if (unit.type.isStatic)
		{
			new ActionHelper(game).clearUnitState(unit);
			game.unitsStaticDead[unit.player.ordinal].add(unit);
		}
		if (unit.type.hasTombstone)
		{
			new TaskRemoveTombstone(game)
					.setIJ(unit.i, unit.j)
					.setTurn(game.numberPlayers() + 1)
					.register();
			game.fieldUnitsDead[unit.i][unit.j] = unit;
		}
		
		unit.player.units.remove(unit);
		game.fieldUnits[unit.i][unit.j] = null;
	}
	
	public int getDecreaseHealth(Unit unit, Unit targetUnit)
	{
		int add = unit.level * 2;
		for (BonusForUnit bonus : unit.type.bonusForUnitAttack)
			if (targetUnit.type == bonus.type)
				add += bonus.value;
				
		// float part = game.random.nextFloat() * 2 - 1;
		float part = 0;
		return (int) Math.min(unit.health / 100f * Math.max(unit.attack + unit.attackDelta * part + add - getUnitDefence(targetUnit), 0), targetUnit.health);
	}
	
	public float getUnitDefence(Unit unit)
	{
		int add = 0;
		final Cell cell = game.fieldCells[unit.i][unit.j];
		for (BonusOnCellGroup bonus : unit.type.bonusOnCellDefence)
			if (cell.type.group == bonus.group)
				add += bonus.value;
		return unit.type.defence + add + unit.level * 2;
	}
	
	public UnitType getKingType(Player player)
	{
		HashMap<MyColor, UnitType> colorToKing = new HashMap<MyColor, UnitType>();
		colorToKing.put(MyColor.BLUE, UnitType.getType("KING_GALAMAR"));
		colorToKing.put(MyColor.GREEN, UnitType.getType("KING_VALADORN"));
		colorToKing.put(MyColor.RED, UnitType.getType("KING_DEMONLORD"));
		colorToKing.put(MyColor.BLACK, UnitType.getType("KING_SAETH"));
		return colorToKing.get(player.color);
	}
	
	public Unit getKing(Player player)
	{
		return getKing(player, getKingType(player));
	}
	
	public Unit getKing(Player player, UnitType kingType)
	{
		for (Unit unit : player.units)
			if (unit.type == kingType)
				return unit;
		return null;
	}
	
}
