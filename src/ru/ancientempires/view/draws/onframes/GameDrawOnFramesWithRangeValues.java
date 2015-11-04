package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDrawMain;

public abstract class GameDrawOnFramesWithRangeValues extends GameDrawOnFrames
{
	
	private int	start;
	private int	end;
	private int	step;
	
	public GameDrawOnFramesWithRangeValues(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawOnFramesWithRangeValues animateRange(int start, int end, int step)
	{
		this.start = start;
		this.end = end;
		this.step = step;
		animate((end - start) / this.step + 1);
		return this;
	}
	
	public GameDrawOnFramesWithRangeValues animateRange(int start, int end)
	{
		return animateRange(start, end, 1);
	}
	
	@Override
	public final void drawOnFrames(Canvas canvas)
	{
		// draw(canvas, this.start + this.step * this.framePass);
		int a = this.frameLeft * this.start + this.framePass * this.end;
		int b = this.frameCount - 1;
		int value = (2 * a / b + 1) / 2;
		draw(canvas, value);
	}
	
	public abstract void draw(Canvas canvas, int value);
	
}
