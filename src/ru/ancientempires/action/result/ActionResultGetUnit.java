package ru.ancientempires.action.result;

public class ActionResultGetUnit extends ActionResult
{
	
	public boolean	canMove;
	public boolean	canAttack;
	public boolean	canRaise;
	
	public boolean[][]	fieldMove;
	public boolean[][]	fieldMoveReal;
	public boolean[][]	fieldAttack;
	public boolean[][]	fieldAttackReal;
	public boolean[][]	fieldRaise;
	public boolean[][]	fieldRaiseReal;
	public int[][]		previousMoveI;
	public int[][]		previousMoveJ;
	
}
