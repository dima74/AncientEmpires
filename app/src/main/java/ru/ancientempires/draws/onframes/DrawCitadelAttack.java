package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.draws.DrawCellAttackPartTwo;

public class DrawCitadelAttack extends DrawOnFramesGroup
{

	private int               i;
	private int               j;
	private DrawBitmapsMoving drawBlast;

	public DrawCitadelAttack(int i, int j)
	{
		this.i = i;
		this.j = j;
		int y = i * A;
		int x = j * A;
		drawBlast = new DrawBitmapsMoving();
		drawBlast.setLineYX((main.iMin - 1) * A, x, y, x);
		drawBlast.setBitmaps(SparksImages().bitmapsDefault);
		drawBlast.animateDelta(6);
		add(drawBlast);
	}

	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);

		if (iFrame() == drawBlast.frameEnd)
		{
			main.campaign.vibrate();
			add(new DrawSnakeMap().animate(20));
			add(new DrawCellAttackPartTwo(i, j));
			game.fieldUnits[i][j] = null;
			return;
		}

		if (!drawBlast.isEnd() && iFrame() % 2 == 0)
			for (int i = 0; i < 3; ++i)
			{
				int framesForBitmap = 2 + main.rnd.nextInt(5); // [2, 6]
				//int framesForBitmap = 6;
				int deltaY = -2 + main.rnd.nextInt(2); // [-2, -1]
				//int deltaY = 0;
				int y0 = drawBlast.y;
				int y1 = y0 + deltaY * framesForBitmap * 4;
				int x = drawBlast.x + main.rnd.nextInt(SparksImages().wDefault - SmokeImages().wSmall + 1);
				//int x = drawBlast.x + i * 4;
				add(new DrawBitmapsMoving()
						.setLineYX(y0, x, y1, x)
						.setBitmaps(SmokeImages().bitmapsSmall)
						.setFramesForBitmap(framesForBitmap)
						.animateRepeat(1));
				//createSimpleSparkSprite(sprBSmoke,
				// heavenFuryBlast.currentX + Renderer.randomToRange(heavenFuryBlast.width - sprBSmoke.width),
				// heavenFuryBlast.currentY,
				// 0,
				// Renderer.randomFromRange(-3, 0),
				// 1,
				// 50 * Renderer.randomToRange(4));
			}
	}

}
