package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawBitmapsMoving;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;

public class GameDrawUnitDie extends GameDrawOnFramesGroup
{
	
	public GameDrawUnitDie(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(int i, int j)
	{
		add(new GameDrawBitmaps(this.gameDraw)
				.setYX(i * GameDraw.A, j * GameDraw.A)
				.setBitmaps(SparksImages.bitmapsDefault)
				.animateRepeat(2));
				
		int startY = i * GameDraw.A;
		int startX = j * GameDraw.A;
		int endY = startY - 3 * 2 * SmokeImages.amountDefault;
		int endX = startX;
		add(new GameDrawBitmapsMoving(this.gameDraw)
				.setLineYX(startY, startX, endY, endX)
				.setBitmaps(SmokeImages.bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1));
	}
	
}
