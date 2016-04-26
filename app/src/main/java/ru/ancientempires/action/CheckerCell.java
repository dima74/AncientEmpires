package ru.ancientempires.action;

import ru.ancientempires.model.Cell;

public interface CheckerCell extends Checker<Cell>
{
	
	@Override
	public boolean check(Cell target);
	
}
