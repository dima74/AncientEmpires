package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Map;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawBuildingSmokes extends GameDrawOnFrames
{
	
	private GameDrawOnFrames[][]	field;
	
	public GameDrawBuildingSmokes(GameDrawMain gameDraw)
	{
		super(gameDraw);
		
		Map map = gameDraw.game.map;
		this.field = new GameDrawOnFrames[map.h][map.w];
		update(gameDraw.game);
	}
	
	@Override
	public boolean update(Game game)
	{
		for (int i = 0; i < this.field.length; i++)
			for (int j = 0; j < this.field.length; j++)
				if (CellImages.isCellSmokes(game.map.getField()[i][j]))
				{
					int startY = (int) (i * GameDraw.A - GameDraw.a * 2);
					int startX = (j + 1) * GameDraw.A - SmokeImages.wSmall;
					int endY = (int) (i * GameDraw.A - GameDraw.a * 21);
					int endX = (j + 1) * GameDraw.A - SmokeImages.wSmall;
					this.field[i][j] = new GameDrawBitmapsMoving(this.gameDraw).animate(0, startY, startX, endY, endX, 40).setBitmaps(SmokeImages.bitmapsSmall);
				}
				else
					this.field[i][j] = null;
		return false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (GameDrawOnFrames[] line : this.field)
			for (GameDrawOnFrames draw : line)
				if (draw != null)
				{
					draw.draw(canvas);
					if (this.gameDraw.iFrame % 2 == 0 && draw.isEndDrawing && this.gameDraw.rnd.nextInt() % 8 == 0)
						draw.reAnimate();
				}
	}
	
}
