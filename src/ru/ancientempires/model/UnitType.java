package ru.ancientempires.model;

import ru.ancientempires.bonuses.BonusForUnit;
import ru.ancientempires.bonuses.BonusOnCellGroup;

public class UnitType
{
	
	private static UnitType[] types;
	
	public static void setUnitTypes(String[] typeStrings)
	{
		UnitType.number = typeStrings.length;
		UnitType.types = new UnitType[UnitType.number];
		for (int i = 0; i < typeStrings.length; i++)
			UnitType.types[i] = new UnitType(typeStrings[i]);
	}
	
	public static void finishInit()
	{
		for (int i = 0; i < UnitType.number; i++)
			UnitType.types[i].ordinal = i;
	}
	
	public static UnitType getType(String name)
	{
		for (UnitType unitType : UnitType.types)
			if (unitType.name.equals(name))
				return unitType;
		return null;
	}
	
	public static UnitType getType(int i)
	{
		return UnitType.types[i];
	}
	
	public static int number;
	
	public String	name;
	public int		ordinal;
	
	public int		baseHealth;
	public float	attack;
	public float	attackDelta;
	public int		defence;
	public int		moveRadius;
	public int		cost;
	
	public boolean[]	captureTypes;
	public boolean[]	repairTypes;
	public boolean[]	destroyingTypes;
	
	public RangeType	attackRange;
	public RangeType	attackRangeReverse;
	
	public RangeType	raiseRange;
	public UnitType		raiseUnit;
	
	public boolean	isFly;
	public boolean	isStatic;
	public boolean	hasTombstone;
	public boolean	canDoTwoActionAfterOne;
	
	public BonusOnCellGroup[]	bonusOnCellWay;
	public BonusOnCellGroup[]	bonusOnCellAttack;
	public BonusOnCellGroup[]	bonusOnCellDefence;
	
	public BonusForUnit[] bonusForUnitAttack;
	
	/*
		Бонусы:
			1. При атаке другого война
				- бонусы от типа
				- бонусы на местности
			2. При получения поля для хода
				а. В начале изменение общего moveRadius
				б. При каждом перемещении между клеточками
	*/
	
	public RangeType	bonusAfterMovingRange;
	public int			bonusForUnitAfterMovingAttack;
	public int			bonusForUnitAfterMovingDefence;
	public int			bonusForUnitAfterAttackAttack;
	public int			bonusForUnitAfterAttackDefence;
	
	public UnitType(String name)
	{
		this.name = name.intern();
	}
	
	public UnitType()
	{}
	
	public UnitType setProperties(UnitType type)
	{
		baseHealth = type.baseHealth;
		attack = type.attack;
		attackDelta = type.attackDelta;
		defence = type.defence;
		moveRadius = type.moveRadius;
		cost = type.cost;
		
		captureTypes = type.captureTypes;
		repairTypes = type.repairTypes;
		attackRange = type.attackRange;
		destroyingTypes = type.destroyingTypes;
		attackRangeReverse = type.attackRangeReverse;
		raiseRange = type.raiseRange;
		raiseUnit = type.raiseUnit;
		
		isFly = type.isFly;
		isStatic = type.isStatic;
		hasTombstone = type.hasTombstone;
		canDoTwoActionAfterOne = type.canDoTwoActionAfterOne;
		
		bonusOnCellWay = type.bonusOnCellWay;
		bonusOnCellAttack = type.bonusOnCellAttack;
		bonusOnCellDefence = type.bonusOnCellDefence;
		bonusForUnitAttack = type.bonusForUnitAttack;
		
		bonusAfterMovingRange = type.bonusAfterMovingRange;
		bonusForUnitAfterMovingAttack = type.bonusForUnitAfterMovingAttack;
		bonusForUnitAfterMovingDefence = type.bonusForUnitAfterMovingDefence;
		bonusForUnitAfterAttackAttack = type.bonusForUnitAfterAttackAttack;
		bonusForUnitAfterAttackDefence = type.bonusForUnitAfterAttackDefence;
		
		return this;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
