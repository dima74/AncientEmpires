package ru.ancientempires.draws.campaign;

import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawUnitAttack extends DrawOnFramesGroup
{
	
	public void start(int i, int j)
	{
		add(new DrawBitmaps()
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(2));
	}
	
}
