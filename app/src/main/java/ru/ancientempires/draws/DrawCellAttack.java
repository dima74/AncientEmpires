package ru.ancientempires.draws;

import android.graphics.Canvas;

import ru.ancientempires.actions.result.ActionResultUnitAttack;
import ru.ancientempires.draws.onframes.DrawBitmaps;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;

public class DrawCellAttack extends DrawOnFramesGroup
{

	private int targetI;
	private int targetJ;
	private int frameUpdateTargetCell = -1;


	/*
	
	public final void sub_6de(Unit var1)
	{
		int var2 = var1.currentY + 24;
		createSimpleSparkSprite(sprSmoke, var1.currentX, var2 - sprSmoke.height, 0, -2, 1, 100);
	
		for(int var3 = 0; var3 < 5; ++var3)
		{
			createSimpleSparkSprite(sprBSmoke, var1.currentX, var2 - sprBSmoke.height, -2 + var3, Renderer.randomFromRange(-4, -1), 1, 50 + 50 * Renderer.randomToRange(4));
		}
	
		createSimpleSparkSprite(sprSpark, var1.currentX, var1.currentY, 0, 0, 1, 100);
	}
	
	*/

	public DrawCellAttack(BaseDrawMain mainBase, ActionResultUnitAttack result, int targetI, int targetJ)
	{
		super(mainBase);
		this.targetI = targetI;
		this.targetJ = targetJ;
		int y = targetI * A;
		int x = targetJ * A;

		add(new DrawBitmaps(mainBase)
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(1));
		frameUpdateTargetCell = frameEnd;

		add(new DrawCellAttackPartTwo(mainBase, targetI, targetJ)
				.increaseFrameStart(frameCount));

		main.cells.keep[targetI][targetJ] = true;
		main.cellsDual.keep[targetI][targetJ] = true;
	}

	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (iFrame() == frameUpdateTargetCell)
		{
			main.cells.keep[targetI][targetJ] = false;
			main.cellsDual.keep[targetI][targetJ] = false;
		}
	}

	@Override
	public void onEnd()
	{
		postUpdateCampaign();
	}

}
