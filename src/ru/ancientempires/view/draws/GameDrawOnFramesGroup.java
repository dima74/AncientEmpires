package ru.ancientempires.view.draws;

import java.util.ArrayList;

import android.graphics.Canvas;

public class GameDrawOnFramesGroup extends GameDrawOnFrames
{
	
	public ArrayList<GameDrawOnFrames>	draws	= new ArrayList<GameDrawOnFrames>();
	
	public GameDrawOnFramesGroup(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		if (this.isDrawing)
			for (GameDrawOnFrames gameDrawOnFrames : this.draws)
				gameDrawOnFrames.draw(canvas);
	}
	
}
