package ru.ancientempires.view.draws;

import java.util.ArrayList;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.model.Unit;
import ru.ancientempires.view.draws.onframes.GameDrawDecreaseHealth;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;

public class GameDrawUnitsHeal extends GameDrawOnFramesGroup
{
	
	public GameDrawUnitsHeal(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(ActionResult result)
	{
		ArrayList<Unit> unitsToHeal = (ArrayList<Unit>) result.getProperty("unitsToHeal");
		ArrayList<Integer> valueToHeal = (ArrayList<Integer>) result.getProperty("valueToHeal");
		
		for (int i = 0; i < unitsToHeal.size(); i++)
		{
			Unit unit = unitsToHeal.get(i);
			GameDrawDecreaseHealth gameDraw = new GameDrawDecreaseHealth(this.gameDraw);
			gameDraw.animate(0, unit.i * GameDraw.A, unit.j * GameDraw.A, +1, valueToHeal.get(i));
			this.draws.add(gameDraw);
		}
	}
	
}
