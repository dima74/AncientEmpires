package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawBitmapsMoving;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;

public class GameDrawUnitDie extends GameDrawOnFramesGroup
{
	
	private int	i;
	private int	j;
	
	public void start(int i, int j)
	{
		this.i = i;
		this.j = j;
		add(new GameDrawBitmaps()
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsDefault)
				.animateRepeat(2));
				
		int startY = i * A;
		int startX = j * A;
		int endY = startY - 3 * 2 * SmokeImages().amountDefault;
		int endX = startX;
		add(new GameDrawBitmapsMoving()
				.setLineYX(startY, startX, endY, endX)
				.setBitmaps(SmokeImages().bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1));
	}
	
	@Override
	public void onEndDraw()
	{
		super.onEndDraw();
		main.gameDrawUnits.updateUnit(i, j);
	}
	
}
