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
		this.baseHealth = type.baseHealth;
		this.attack = type.attack;
		this.attackDelta = type.attackDelta;
		this.defence = type.defence;
		this.moveRadius = type.moveRadius;
		this.cost = type.cost;
		
		this.captureTypes = type.captureTypes;
		this.repairTypes = type.repairTypes;
		this.attackRange = type.attackRange;
		this.destroyingTypes = type.destroyingTypes;
		this.attackRangeReverse = type.attackRangeReverse;
		this.raiseRange = type.raiseRange;
		this.raiseUnit = type.raiseUnit;
		
		this.isFly = type.isFly;
		this.isStatic = type.isStatic;
		this.hasTombstone = type.hasTombstone;
		this.canDoTwoActionAfterOne = type.canDoTwoActionAfterOne;
		
		this.bonusOnCellWay = type.bonusOnCellWay;
		this.bonusOnCellAttack = type.bonusOnCellAttack;
		this.bonusOnCellDefence = type.bonusOnCellDefence;
		this.bonusForUnitAttack = type.bonusForUnitAttack;
		
		this.bonusAfterMovingRange = type.bonusAfterMovingRange;
		this.bonusForUnitAfterMovingAttack = type.bonusForUnitAfterMovingAttack;
		this.bonusForUnitAfterMovingDefence = type.bonusForUnitAfterMovingDefence;
		this.bonusForUnitAfterAttackAttack = type.bonusForUnitAfterAttackAttack;
		this.bonusForUnitAfterAttackDefence = type.bonusForUnitAfterAttackDefence;
		
		return this;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
}
