package ru.ancientempires.view.draws.campaign;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesWithRangeFloat;

public class GameDrawCameraMove extends GameDrawOnFramesGroup
{
	
	public static float delta;
	
	private float	startOffsetY;
	private float	startOffsetX;
	private float	endOffsetY;
	private float	endOffsetX;
	
	public GameDrawCameraMove(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(int iEnd, int jEnd)
	{
		this.startOffsetY = this.gameDraw.offsetY;
		this.startOffsetX = this.gameDraw.offsetX;
		
		this.endOffsetY = -iEnd * GameDraw.A - GameDraw.A / 2 + this.gameDraw.visibleMapH / GameDraw.mapScale / 2;
		this.endOffsetX = -jEnd * GameDraw.A - GameDraw.A / 2 + this.gameDraw.visibleMapW / GameDraw.mapScale / 2;
		this.endOffsetY = Math.max(this.gameDraw.minOffsetY, Math.min(this.gameDraw.maxOffsetY, this.endOffsetY));
		this.endOffsetX = Math.max(this.gameDraw.minOffsetX, Math.min(this.gameDraw.maxOffsetX, this.endOffsetX));
		
		float deltaY = this.endOffsetY - this.startOffsetY;
		float deltaX = this.endOffsetX - this.startOffsetX;
		int frameCount = Math.round(Math.max(Math.abs(deltaY), Math.abs(deltaX)) * GameDraw.mapScale / GameDrawCameraMove.delta);
		
		add(new GameDrawOnFramesWithRangeFloat(this.gameDraw)
		{
			@Override
			public void draw(Canvas canvas, float value)
			{
				this.gameDraw.nextOffsetY = value;
			}
		}.animateRange(this.startOffsetY, this.endOffsetY, deltaY / frameCount));
		add(new GameDrawOnFramesWithRangeFloat(this.gameDraw)
		{
			@Override
			public void draw(Canvas canvas, float value)
			{
				this.gameDraw.nextOffsetX = value;
			}
		}.animateRange(this.startOffsetX, this.endOffsetX, deltaX / frameCount));
		
		this.gameDraw.inputAlgorithmMain.tapWithoutAction(iEnd, jEnd);
	}
	
}
