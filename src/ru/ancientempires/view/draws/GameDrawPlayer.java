package ru.ancientempires.view.draws;

import java.util.ArrayList;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;

public class GameDrawPlayer extends GameDraw
{
	
	private ArrayList<GameDrawOnFrames> draws = new ArrayList<GameDrawOnFrames>();
	
	public void add(GameDrawOnFrames gameDraw)
	{
		for (int i = 0; i < draws.size(); i++)
			if (draws.get(i).isEndDrawing)
			{
				draws.set(i, draws.get(draws.size() - 1));
				draws.remove(draws.size() - 1);
			}
		draws.add(gameDraw);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (GameDraw gameDraw : draws)
			gameDraw.draw(canvas);
	}
	
}
