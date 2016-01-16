package ru.ancientempires.view.draws;

import java.util.ArrayList;

import ru.ancientempires.action.result.ActionResultGameEndTurn;
import ru.ancientempires.model.Unit;
import ru.ancientempires.view.draws.onframes.GameDrawNumberSinus;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;

public class GameDrawUnitsHeal extends GameDrawOnFramesGroup
{
	
	public void start(ActionResultGameEndTurn result)
	{
		ArrayList<Unit> unitsToHeal = result.unitsToHeal;
		ArrayList<Integer> valueToHeal = result.valueToHeal;
		
		for (int i = 0; i < unitsToHeal.size(); i++)
		{
			Unit unit = unitsToHeal.get(i);
			add(new GameDrawNumberSinus()
					.animate(unit.i * A, unit.j * A, +1, valueToHeal.get(i)));
		}
	}
	
}
