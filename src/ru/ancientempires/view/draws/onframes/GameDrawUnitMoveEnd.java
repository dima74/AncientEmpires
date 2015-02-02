package ru.ancientempires.view.draws.onframes;

import java.util.ArrayList;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.model.Unit;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawUnitMoveEnd extends GameDrawOnFrames
{
	
	private ArrayList<GameDrawBitmaps>	draws;
	
	public GameDrawUnitMoveEnd(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(ActionResult result, int frameToStart)
	{
		this.draws = new ArrayList<GameDrawBitmaps>();
		if ((int) result.getProperty("sign") == +1)
		{
			Unit[] units = (Unit[]) result.getProperty("units");
			for (Unit unit : units)
			{
				GameDrawBitmaps gameDrawBitmaps = new GameDrawBitmaps(this.gameDraw).setBitmaps(SparksImages.bitmapsDefault);
				gameDrawBitmaps.animate(frameToStart, unit.i * GameDraw.A, unit.j * GameDraw.A, GameDrawBitmaps.FRAMES_ANIMATE_SHORT);
				this.draws.add(gameDrawBitmaps);
			}
		}
		animate(frameToStart, GameDrawBitmaps.FRAMES_ANIMATE_SHORT);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		for (GameDrawBitmaps gameDrawBitmaps : this.draws)
			gameDrawBitmaps.draw(canvas);
	}
	
}
