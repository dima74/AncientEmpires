package ru.ancientempires.action.result;

public class AttackResult
{
	
	public int		i;
	public int		j;
	
	public int		targetI;
	public int		targetJ;
	
	public int		decreaseHealth;
	public boolean	isTargetLive;
	public boolean	isLevelUp;
	
	public int		effectSign;
	
	public AttackResult(int i, int j, int targetI, int targetJ, int decreaseHealth, boolean isTargetLive, boolean isLevelUp, int effectSign)
	{
		this.i = i;
		this.j = j;
		this.targetI = targetI;
		this.targetJ = targetJ;
		this.decreaseHealth = decreaseHealth;
		this.isTargetLive = isTargetLive;
		this.isLevelUp = isLevelUp;
		this.effectSign = effectSign;
	}
	
}
