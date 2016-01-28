package ru.ancientempires.model;

import java.util.Set;

import ru.ancientempires.action.CheckerUnit;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.bonuses.BonusForUnit;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.handler.IGameHandler;

public class Unit extends IGameHandler
{
	
	public UnitType	type;
	public Player	player;
	
	public int	i;
	public int	j;
	public int	health	= 100;
	public int	level;
	public int	experience;
	
	public Set<Bonus>	bonuses;
	public int			cost;
	
	public boolean	isMove;
	public boolean	isTurn;
	
	public void setTurn()
	{
		isMove = true;
		isTurn = true;
	}
	
	/* Два варианта создания нового объекта
	 1. При загрузке игры: 
	 	экземпляр 
	 -> свойства из unitType
	 -> загружаем оставшиеся свойства
	 
	 2. При покупки в замке/воскрешении:
	 	экземпляр
	 -> свойства из unitType
	 */
	
	public Unit(UnitType type, Player player, Game game)
	{
		setGame(game);
		this.type = type;
		this.player = player;
		setProperties(type);
	}
	
	public Unit setProperties(Unit defaultUnit)
	{
		health = defaultUnit.health;
		level = defaultUnit.level;
		experience = defaultUnit.experience;
		
		isMove = defaultUnit.isMove;
		isTurn = defaultUnit.isTurn;
		return this;
	}
	
	public Unit setProperties(UnitType type)
	{
		cost = type.cost;
		return this;
	}
	
	public Cell getCell()
	{
		return game.fieldCells[i][j];
	}
	
	@Override
	public boolean equals(Object o)
	{
		Unit unit = (Unit) o;
		if (type != unit.type)
			return false;
		if (player.ordinal != unit.player.ordinal)
			return false;
		if (i != unit.i)
			return false;
		if (j != unit.j)
			return false;
		if (health != unit.health)
			return false;
		if (level != unit.level)
			return false;
		if (experience != unit.experience)
			return false;
		if (!bonuses.equals(unit.bonuses))
			return false;
		if (cost != unit.cost)
			return false;
		if (isMove != unit.isMove)
			return false;
		if (isTurn != unit.isTurn)
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (%d %d) %d %d (%b %b) ", type.name, i, j, player.ordinal, health, isMove, isTurn);
	}
	
	public int var_8f7;
	
	public int sub_462(Unit targetUnit)
	{
		return this.sub_462(j, i, targetUnit);
	}
	
	public int sub_462(int j, int i, Unit targetUnit)
	{
		// System.out.println(attack * 2 + " " + defence + " " + getOffenceBonusAgainstUnitEx(targetUnit, j, i) + " " + getDefenceBonusAgainstUnitEx(targetUnit, j, i));
		return (int) ((attack * 2 + defence + getOffenceBonusAgainstUnitEx(targetUnit, j, i) + getDefenceBonusAgainstUnitEx(targetUnit, j, i)) * health / 100);
	}
	
	private int getOffenceBonusAgainstUnitEx(Unit targetUnit, int j, int i)
	{
		int add = 0;
		if (targetUnit != null)
			for (BonusForUnit bonus : type.bonusForUnitAttack)
				if (targetUnit.type == bonus.type)
					add += bonus.value;
		return level * 2 + add;
	}
	
	private int getDefenceBonusAgainstUnitEx(Unit targetUnit, int j, int i)
	{
		int add = 0;
		Cell cell = game.fieldCells[i][j];
		for (BonusOnCellGroup bonus : type.bonusOnCellDefence)
			if (cell.type.group == bonus.group)
				add += bonus.value;
		return level * 2 + game.fieldCells[i][j].type.defense + add;
	}
	
	public Unit[] getUnitsWithinRange(int x, int y, int minRange, int maxRange, byte b)
	{
		Range type = new Range(null, minRange, maxRange);
		return new ActionHelper(game).getUnitsInRange(y, x, type, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return player.team != targetUnit.player.team;
			}
		});
	}
	
	public Unit[] getUnitsToAttack(int x, int y)
	{
		return new ActionHelper(game).getUnitsInRange(y, x, type.attackRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return player.team != targetUnit.player.team;
			}
		});
	}
	
	public Unit[] getUnitsToRaise(int x, int y)
	{
		return new ActionHelper(game).getUnitsInRange(game.fieldUnitsDead, y, x, type.raiseRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return true;
			}
		});
	}
	
}
