package ru.ancientempires.model;

import ru.ancientempires.action.handlers.ActionHandlerHelper;
import ru.ancientempires.action.handlers.CheckerUnit;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.bonuses.BonusForUnit;
import ru.ancientempires.bonuses.BonusOnCellGroup;

public class Unit
{
	
	public static Unit defaultUnit = new Unit();
	
	public UnitType	type;
	public Player	player;
	
	public int	i;
	public int	j;
	public int	health;
	public int	level;
	public int	experience;
	
	// Определяются типом + бонусами
	public float	attack;
	public float	attackDelta;
	public int		defence;
	public int		moveRadius;
	public int		cost;
	
	public boolean	isMove;
	public boolean	isTurn;
	
	public boolean checkFloating()
	{
		Unit floatingUnit = GameHandler.game.floatingUnit;
		return (floatingUnit == null || floatingUnit.i == i && floatingUnit.j == j)
				&& this != floatingUnit;
	}
	
	public void setTurn()
	{
		isMove = true;
		isTurn = true;
	}
	
	/* Три варианта создания нового объекта
	 0. defaultUnit
	 	экземпляр
	 -> загружаем все свойства
	 
	 1. При загрузке игры: 
	 	экземпляр 
	 -> свойства из defaultUnit 
	 -> свойства из unitType
	 -> загружаем оставшиеся свойства
	 
	 2. При покупки в замке/воскрешении:
	 	экземпляр
	 -> свойства из defaultType и UnitType
	 */
	
	private Unit()
	{}
	
	public Unit(UnitType type, Player player)
	{
		this.type = type;
		this.player = player;
		setProperties(Unit.defaultUnit);
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
		attack = type.attack;
		attackDelta = type.attackDelta;
		defence = type.defence;
		moveRadius = type.moveRadius;
		cost = type.cost;
		
		return this;
	}
	
	@Override
	public String toString()
	{
		return type.name + " (" + i + ", " + j + ") " + player.ordinal + " " + health;
	}
	
	public int var_8f7;
	
	public int sub_462(Unit targetUnit)
	{
		return this.sub_462(j, i, targetUnit);
	}
	
	public int sub_462(int j, int i, Unit targetUnit)
	{
		System.out.println(attack * 2 + " " + defence + " " + getOffenceBonusAgainstUnitEx(targetUnit, j, i) + " " + getDefenceBonusAgainstUnitEx(targetUnit, j, i));
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
		Cell cell = GameHandler.fieldCells[i][j];
		for (BonusOnCellGroup bonus : type.bonusOnCellDefence)
			if (cell.type.group == bonus.group)
				add += bonus.value;
		return level * 2 + GameHandler.fieldCells[i][j].type.defense + add;
	}
	
	public Unit[] getUnitsWithinRange(int x, int y, int minRange, int maxRange, byte b)
	{
		RangeType type = new RangeType();
		type.radius = maxRange;
		type.field = new boolean[maxRange * 2 + 1][maxRange * 2 + 1];
		for (int i = -maxRange; i <= maxRange; i++)
			for (int j = -maxRange; j <= maxRange; j++)
				if (Math.abs(i) + Math.abs(j) <= maxRange)
					type.field[maxRange + i][maxRange + j] = true;
		type.field[maxRange][maxRange] = false;
		
		return ActionHandlerHelper.getUnitsInRange(y, x, type, new CheckerUnit()
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
		return ActionHandlerHelper.getUnitsInRange(y, x, type.attackRange, new CheckerUnit()
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
		return ActionHandlerHelper.getUnitsInRange(GameHandler.fieldDeadUnits, y, x, type.raiseRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return true;
			}
		});
	}
	
}
