package ru.ancientempires.actions;

import ru.ancientempires.model.Cell;

public interface CheckerCell extends Checker<Cell>
{

	@Override
	public boolean check(Cell target);

}
