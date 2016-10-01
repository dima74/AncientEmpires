package ru.ancientempires.draws.onframes;

import android.graphics.Bitmap;

import ru.ancientempires.draws.BaseDrawMain;

public class DrawLevelUp extends DrawOnFramesGroup {

	// 2 2 2 2 40
	// 21 17 13 9 5

	private static final int FRAME_ANIMATE        = 48;
	private static final int FRAME_ANIMATE_MOTION = 16;
	private static final int FRAME_ANIMATE_STATIC = 32;

	public DrawLevelUp(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public DrawLevelUp animate(int y, int x) {
		int levelUpX = x + (A - Images().levelUpW) / 2;
		int levelUpYStart = (int) (y - A + 20 * a);
		int levelUpYEnd = (int) (y - A + 5 * a);

		int amountLevelUps = 4;
		for (int i = amountLevelUps; i >= 0; i--)
			add(new DrawBitmapsMoving(mainBase)
					.setLineYX(levelUpYStart, levelUpX, levelUpYEnd, levelUpX)
					.setBitmaps(new Bitmap[]
							{
									Images().levelUp
							})
					.setFramesForBitmap(DrawLevelUp.FRAME_ANIMATE_MOTION)
					.animateRepeat(1)
					.increaseFrameStart((int) (i * 3 * a)));
		add(new DrawBitmap(mainBase)
				.setYX(levelUpYEnd, levelUpX)
				.setBitmap(Images().levelUp)
				.animate(DrawLevelUp.FRAME_ANIMATE_STATIC)
				.increaseFrameStart(DrawLevelUp.FRAME_ANIMATE_MOTION));
		return this;
	}
}
