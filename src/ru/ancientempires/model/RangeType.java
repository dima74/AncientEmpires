package ru.ancientempires.model;

import java.util.ArrayList;

public class RangeType
{
	
	private static RangeType[]		types;
	public static final RangeType	MAX_ATTACK	= new RangeType();
	
	public static void setAttackTypes(ArrayList<RangeType> types)
	{
		RangeType.types = types.toArray(new RangeType[0]);
		RangeType.number = RangeType.types.length;
		for (int i = 0; i < RangeType.number; i++)
			RangeType.types[i].ordinal = i;
			
		int maxRadius = 0;
		for (RangeType type : types)
			maxRadius = Math.max(maxRadius, type.radius);
		RangeType.MAX_ATTACK.radius = maxRadius;
		RangeType.MAX_ATTACK.field = new boolean[maxRadius * 2 + 1][maxRadius * 2 + 1];
		for (RangeType type : types)
			for (int i = -type.radius; i <= type.radius; i++)
				for (int j = -type.radius; j <= type.radius; j++)
					if (type.field[type.radius + i][type.radius + j])
						RangeType.MAX_ATTACK.field[maxRadius + i][maxRadius + j] = true;
	}
	
	public static RangeType getType(String name)
	{
		for (RangeType type : RangeType.types)
			if (type.name.equals(name))
				return type;
		return null;
	}
	
	public static int number;
	
	public String	name;
	public int		ordinal;
	
	public int			radius;
	public boolean[][]	field;
	
	public boolean checkAccess(Unit unit, Unit targetUnit)
	{
		return checkAccess(unit, targetUnit.i, targetUnit.j);
	}
	
	public boolean checkAccess(Unit unit, int targetI, int targetJ)
	{
		int size = field.length;
		int relI = targetI - unit.i + radius;
		int relJ = targetJ - unit.j + radius;
		return 0 <= relI && relI < size && 0 <= relJ && relJ < size && field[relI][relJ];
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for (boolean[] element : field)
		{
			for (boolean element2 : element)
				s += element2 ? '1' : '0';
			s += "\n";
		}
		return "RangeType [" + name + "]\n" + s;
	}
	
}
