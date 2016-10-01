package ru.ancientempires.draws.campaign;

import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawBitmapsMoving;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawUnitDie extends DrawOnFramesGroup {

	private int i;
	private int j;

	public DrawUnitDie(BaseDrawMain mainBase, int i, int j) {
		super(mainBase);
		this.i = i;
		this.j = j;
		add(new DrawBitmaps(mainBase)
				.setYX(i * A, j * A)
				.setBitmaps(SparksImages().bitmapsDefault)
				.animateRepeat(1));

		int startY = i * A;
		int startX = j * A;
		int endY = startY - 3 * 2 * SmokeImages().amountDefault;
		int endX = startX;
		add(new DrawBitmapsMoving(mainBase)
				.setLineYX(startY, startX, endY, endX)
				.setBitmaps(SmokeImages().bitmapsDefault)
				.setFramesForBitmap(4)
				.animateRepeat(1));
	}

	@Override
	public void onEnd() {
		super.onEnd();
		main.units.updateUnit(i, j);
	}
}
