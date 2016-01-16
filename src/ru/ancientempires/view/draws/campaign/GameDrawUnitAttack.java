package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;

public class GameDrawUnitAttack extends GameDrawOnFramesGroup
{
	
	public void start(int i, int j)
	{
		add(new GameDrawBitmaps()
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(2));
	}
	
}
