package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.draws.BaseDrawMain;

public abstract class DrawOnFramesWithRangeFloat extends DrawOnFrames
{

	private float start;
	private float end;
	private float step;

	public DrawOnFramesWithRangeFloat(BaseDrawMain mainBase)
	{
		super(mainBase);
	}

	public DrawOnFramesWithRangeFloat animateRange(float start, float end, float step)
	{
		this.start = start;
		this.end = end;
		this.step = step;
		animate(Math.round((end - start) / this.step + 1));
		return this;
	}

	public DrawOnFramesWithRangeFloat animateRange(float start, float end)
	{
		return animateRange(start, end, 1);
	}

	@Override
	public void drawOnFrames(Canvas canvas)
	{
		float a = (frameCount - 1 - framePass) * start + framePass * end;
		float b = frameCount - 1;
		float value = b == 0 ? start : a / b;
		draw(canvas, value);
	}

	public abstract void draw(Canvas canvas, float value);

}
