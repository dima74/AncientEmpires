package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;

public abstract class GameDrawOnFramesWithRangeValues extends GameDrawOnFrames
{
	
	public int	start;
	public int	end;
	public int	step;
	
	public GameDrawOnFramesWithRangeValues animateRange(int start, int end, int step)
	{
		this.start = start;
		this.end = end;
		this.step = step;
		animate(Math.abs(end - start) / this.step + 1);
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
		int a = frameLeft * start + framePass * end;
		int b = frameCount - 1;
		int value = b == 0 ? start : (2 * a / b + 1) / 2;
		draw(canvas, value);
	}
	
	public abstract void draw(Canvas canvas, int value);
	
}
