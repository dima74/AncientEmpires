package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.view.GameView;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import android.graphics.Canvas;

public class GameDrawCameraMove extends GameDrawOnFrames
{
	
	private static final float	DELTA	= GameDraw.a * 2;
	
	private int					startOffsetY;
	private int					startOffsetX;
	private int					endOffsetY;
	private int					endOffsetX;
	
	public GameDrawCameraMove(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(int iEnd, int jEnd)
	{
		int availableY = this.gameDraw.startActionY - this.gameDraw.gameDrawInfoH;
		int availableX = GameView.w;
		
		int newOffsetY = -iEnd * GameDraw.A - GameDraw.A / 2 + availableY / 2;
		int newOffsetX = -jEnd * GameDraw.A - GameDraw.A / 2 + availableX / 2;
		
		this.endOffsetY = newOffsetY + this.gameDraw.gameDrawInfoH;
		this.endOffsetX = newOffsetX;
		
		this.endOffsetY = Math.max(this.gameDraw.minOffsetY, Math.min(this.gameDraw.maxOffsetY, this.endOffsetY));
		this.endOffsetX = Math.max(this.gameDraw.minOffsetX, Math.min(this.gameDraw.maxOffsetX, this.endOffsetX));
		
		this.startOffsetY = this.gameDraw.offsetY;
		this.startOffsetX = this.gameDraw.offsetX;
		
		int deltaY = this.endOffsetY - this.startOffsetY;
		int deltaX = this.endOffsetX - this.startOffsetX;
		animate(0, (int) (Math.max(Math.abs(deltaY), Math.abs(deltaX)) / GameDrawCameraMove.DELTA));
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (this.isDrawing)
		{
			int frameLeft = this.gameDraw.iFrame - this.frameStart;// прошло
			int framePass = this.frameEnd - this.gameDraw.iFrame;// осталось TODO перевод
			this.gameDraw.nextOffsetY = (this.startOffsetY * framePass + this.endOffsetY * frameLeft) / this.frameLength;
			this.gameDraw.nextOffsetX = (this.startOffsetX * framePass + this.endOffsetX * frameLeft) / this.frameLength;
		}
		if (this.gameDraw.iFrame == this.frameEnd - 1)
		{
			this.gameDraw.nextOffsetY = this.endOffsetY;
			this.gameDraw.nextOffsetX = this.endOffsetX;
		}
	}
	
}
