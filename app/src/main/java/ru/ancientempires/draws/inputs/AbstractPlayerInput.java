package ru.ancientempires.draws.inputs;

public abstract class AbstractPlayerInput extends AbstractInput
{
	
	public void beginTurn() {}
	
	public abstract void tap(int i, int j);
	
	public abstract void endTurn();
	
}
