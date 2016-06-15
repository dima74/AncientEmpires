package ru.ancientempires.actions.result;

import java.util.ArrayList;

import ru.ancientempires.actions.BuyStatus;
import ru.ancientempires.model.Unit;

public class ActionResultGetCellBuy extends ActionResult
{
	
	public Unit[]      units;
	public BuyStatus[] statuses;

	public ActionResultGetCellBuy(ArrayList<Unit> units)
	{
		this.units = units.toArray(new Unit[0]);
		statuses = new BuyStatus[units.size()];
	}
	
}
