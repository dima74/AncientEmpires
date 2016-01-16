package ru.ancientempires.action.result;

import java.util.ArrayList;

import ru.ancientempires.model.Unit;

public class ActionResultGetCellBuy extends ActionResult
{
	
	public Unit[]		units;
	public boolean[]	isAvailable;
	
	public ActionResultGetCellBuy(ArrayList<Unit> units, boolean[] isAvailable)
	{
		this.units = units.toArray(new Unit[0]);
		this.isAvailable = isAvailable;
	}
	
}
