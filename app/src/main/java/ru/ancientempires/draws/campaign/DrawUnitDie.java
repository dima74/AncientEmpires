package ru.ancientempires.draws.campaign;

import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawBitmapsMoving;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawUnitDie extends DrawOnFramesGroup
{
	
	private int	i;
	private int	j;
				
	public DrawUnitDie(int i, int j)
	{
		this.i = i;
		this.j = j;
		add(new DrawBitmaps()
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsDefault)
				.animateRepeat(1));
				
		int startY = i * A;
		int startX = j * A;
		int endY = startY - 3 * 2 * SmokeImages().amountDefault;
		int endX = startX;
		add(new DrawBitmapsMoving()
				.setLineYX(startY, startX, endY, endX)
				.setBitmaps(SmokeImages().bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1));
	}
	
	@Override
	public void onEnd()
	{
		super.onEnd();
		main.units.updateUnit(i, j);
	}
	
}
