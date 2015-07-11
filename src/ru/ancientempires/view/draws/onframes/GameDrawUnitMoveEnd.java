package ru.ancientempires.view.draws.onframes;

import java.util.ArrayList;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.model.Unit;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawUnitMoveEnd extends GameDrawOnFramesGroup
{
	
	public GameDrawUnitMoveEnd(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(ActionResult result, int frameToStart)
	{
		this.draws = new ArrayList<GameDrawOnFrames>();
		if ((int) result.getProperty("sign") == +1)
		{
			Unit[] units = (Unit[]) result.getProperty("units");
			for (Unit unit : units)
			{
				GameDrawBitmaps gameDrawBitmaps = new GameDrawBitmaps(this.gameDraw)
						.setYX(unit.i * GameDraw.A, unit.j * GameDraw.A)
						.setBitmaps(SparksImages.bitmapsDefault)
						.animateRepeat(frameToStart, 1);
				this.draws.add(gameDrawBitmaps);
			}
			if (units.length > 0)
				animate(frameToStart, this.draws.get(0).frameLength);
		}
	}
	
}
