package ru.ancientempires.draws.campaign;

import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawUnitAttack extends DrawOnFramesGroup
{
	
	public DrawUnitAttack(int i, int j)
	{
		main.units.field[i][j].idleAnimation(16);
		add(new DrawBitmaps()
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(2));
	}
	
}
