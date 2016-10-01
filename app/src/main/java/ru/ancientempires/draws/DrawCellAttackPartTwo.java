package ru.ancientempires.draws;

import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawBitmapsMoving;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawCellAttackPartTwo extends DrawOnFramesGroup {

	public DrawCellAttackPartTwo(BaseDrawMain mainBase, int targetI, int targetJ) {
		super(mainBase);
		int y = targetI * A;
		int x = targetJ * A;

		int smokeY = y + 24 - SmokeImages().hDefault;
		add(new DrawBitmapsMoving(mainBase)
				.setLineYX(smokeY, x, smokeY - 16, x) // SmokeImages().wDefault == 24
				.setBitmaps(SmokeImages().bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1));

		for (int i = 0; i < 5; ++i) {
			int framesForBitmap = 2 + random.nextInt(7);
			int framesCount = framesForBitmap * 4;
			int deltaY = -2 + random.nextInt(2); // [-2,-1]
			int deltaX = -2 + i;

			int startY = y + 24 - SmokeImages().hSmall;
			int startX = x + (24 - SmokeImages().wSmall) / 2;
			int endY = startY + (framesCount - 1) * deltaY;
			int endX = startX + (framesCount - 1) * deltaX / 2;
			add(new DrawBitmapsMoving(mainBase)
					.setLineYX(startY, startX, endY, endX)
					.setBitmaps(SmokeImages().bitmapsSmall)
					.setFramesForBitmap(framesForBitmap)
					.animateRepeat(1));
		}

		add(new DrawBitmaps(mainBase)
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1));
	}
}
