package ru.ancientempires.draws;

import java.util.ArrayList;

import ru.ancientempires.action.result.ActionResultGameEndTurn;
import ru.ancientempires.draws.onframes.DrawNumberSinus;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;
import ru.ancientempires.model.Unit;

public class DrawUnitsHeal extends DrawOnFramesGroup
{
	
	public void start(ActionResultGameEndTurn result)
	{
		ArrayList<Unit> unitsToHeal = result.unitsToHeal;
		ArrayList<Integer> valueToHeal = result.valueToHeal;
		
		for (int i = 0; i < unitsToHeal.size(); i++)
		{
			Unit unit = unitsToHeal.get(i);
			add(new DrawNumberSinus()
					.animate(unit.i * A, unit.j * A, +1, valueToHeal.get(i)));
		}
	}
	
}