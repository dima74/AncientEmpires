package ru.ancientempires.actions;

import ru.ancientempires.model.Unit;

public interface CheckerUnit extends Checker<Unit> {

	@Override
	public boolean check(Unit target);

}
