package ru.ancientempires.view;

import ru.ancientempires.model.Game;
import android.graphics.Canvas;

public interface IGameDraw
{
	
	public void draw(Canvas canvas);
	
	public boolean update(Game game);
	
}
