package ru.ancientempires.view.draws.campaign;

import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import android.graphics.Canvas;

public class GameDrawBlackScreen extends GameDrawOnFrames
{
	
	private int	alphaStart	= 0;
	private int	alphaEnd	= 0;
	
	public GameDrawBlackScreen(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(int alphaStart, int alphaEnd)
	{
		this.alphaStart = alphaStart;
		this.alphaEnd = alphaEnd;
		animate(0, 32);
	}
	
	public void blackScreen()
	{
		this.frameStart = -1;
		this.frameEnd = -1;
		this.frameLength = 0;
		this.alphaEnd = 255;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (this.isEndDrawing)
		{
			canvas.drawColor(this.alphaEnd << 24);
			return;
		}
		int framePass = this.gameDraw.iFrame - this.frameStart;
		int frameLeft = this.frameEnd - this.gameDraw.iFrame;
		// int alpha = this.isEndDrawing ? this.alphaEnd : (frameLeft * this.alphaStart + framePass * this.alphaEnd) / this.frameLength;
		int alpha = this.alphaStart + (this.alphaEnd - this.alphaStart) * framePass / (this.frameLength - 1);
		canvas.drawColor(alpha << 24);
	}
	
}
