package ru.ancientempires.handler;

import java.util.HashMap;

import ru.ancientempires.MyColor;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
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
		int attackBonus = unit.getBonusAttack(targetUnit);
		int attack = game.random.nextInt(unit.type.attackMax - unit.type.attackMin) + unit.type.attackMin;
		// int attack = unit.type.attackMin;
		return Math.min(unit.health * Math.max(attack + attackBonus - getUnitDefence(targetUnit, unit), 0) / 100, targetUnit.health);
	}
	
	public int getUnitDefence(Unit unit, Unit fromUnit)
	{
		int defenceBonus = unit.getBonusDefence(fromUnit);
		int defence = unit.type.defence;
		return defence + defenceBonus;
	}
	
	public UnitType getKingType(Player player)
	{
		HashMap<MyColor, UnitType> colorToKing = new HashMap<MyColor, UnitType>();
		colorToKing.put(MyColor.BLUE, game.rules.getUnitType("KING_GALAMAR"));
		colorToKing.put(MyColor.GREEN, game.rules.getUnitType("KING_VALADORN"));
		colorToKing.put(MyColor.RED, game.rules.getUnitType("KING_DEMONLORD"));
		colorToKing.put(MyColor.BLACK, game.rules.getUnitType("KING_SAETH"));
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
