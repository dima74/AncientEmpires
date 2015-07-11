package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.images.SparksImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;

public class GameDrawUnitAttack extends GameDrawOnFramesGroup
{
	
	public GameDrawUnitAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(int i, int j)
	{
		GameDrawOnFrames gameDraw = new GameDrawBitmaps(this.gameDraw)
				.setYX(i * GameDraw.A, j * GameDraw.A)
				.setBitmaps(SparksImages.bitmapsAttack)
				.animateRepeat(0, 2);
		this.draws.add(gameDraw);
		animate(0, gameDraw.frameEnd - this.gameDraw.iFrame);
	}
	
}
