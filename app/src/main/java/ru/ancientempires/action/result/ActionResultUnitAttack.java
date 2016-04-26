package ru.ancientempires.action.result;

import ru.ancientempires.model.Unit;

public class ActionResultUnitAttack extends ActionResult
{
	
	public boolean		isAttackUnit;
	public AttackResult	attackResultDirect;
	public boolean		isReverseAttack;
	public AttackResult	attackResultReverse;
	public Unit[]		unitsToUpdate;
	
}
