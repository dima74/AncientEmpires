package ru.ancientempires.draws.campaign;

import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawUnitAttack extends DrawOnFramesGroup
{
	
	public DrawUnitAttack(BaseDrawMain mainBase, int i, int j)
	{
		super(mainBase);
		main.units.field[i][j].idleAnimation(16);
		add(new DrawBitmaps(mainBase)
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(2));
	}
	
}
