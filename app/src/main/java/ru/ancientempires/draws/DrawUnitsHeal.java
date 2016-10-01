package ru.ancientempires.draws;

import java.util.ArrayList;

import ru.ancientempires.actions.result.ActionResultGameEndTurn;
import ru.ancientempires.draws.onframes.DrawNumberSinus;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;
import ru.ancientempires.model.Unit;

public class DrawUnitsHeal extends DrawOnFramesGroup {

	public DrawUnitsHeal(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public DrawUnitsHeal start(ActionResultGameEndTurn result) {
		ArrayList<Unit> unitsToHeal = result.unitsToHeal;
		ArrayList<Integer> valueToHeal = result.valueToHeal;

		for (int i = 0; i < unitsToHeal.size(); i++) {
			Unit unit = unitsToHeal.get(i);
			add(new DrawNumberSinus(mainBase)
					.animate(unit.i * A, unit.j * A, +1, valueToHeal.get(i)));
		}
		return this;
	}

}
