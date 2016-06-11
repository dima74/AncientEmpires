package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.action.result.ActionResultUnitAttack;
import ru.ancientempires.action.result.AttackResult;

public class DrawUnitAttackMain extends DrawOnFramesGroup
{
	
	public static final int FRAMES_BETWEEN_ANIMATES = 32;    // GameDrawDecreaseHealth.FRAMES_ANIMATE * 2 / 3;

	private DrawUnitAttack drawDirect;
	private DrawUnitAttack drawReverse;

	private boolean isReverseAttack;
	private boolean isUnitDie;

	public int frameToStartPartTwo;
	public int framesBeforePartTwo;

	public DrawUnitAttackMain()
	{
		drawDirect = new DrawUnitAttack().setDirect();
		drawReverse = new DrawUnitAttack();
	}
	
	public DrawUnitAttackMain start(ActionResultUnitAttack result)
	{
		AttackResult resultDirect = result.attackResultDirect;
		isUnitDie = !resultDirect.isTargetLive;
		
		drawDirect.initPartOne(resultDirect);
		framesBeforePartTwo = drawDirect.frameCount - 16;
		
		isReverseAttack = result.isReverseAttack;
		if (isReverseAttack)
		{
			AttackResult resultReverse = result.attackResultReverse;
			drawReverse.initPartOne(resultReverse);
			drawReverse.increaseFrameStart(DrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES);
			framesBeforePartTwo = DrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES + drawReverse.frameCount - 16;
			isUnitDie |= !resultReverse.isTargetLive;
		}
		
		draws.clear();
		add(drawDirect.initPartTwo(framesBeforePartTwo));
		if (isReverseAttack)
			add(drawReverse.initPartTwo(framesBeforePartTwo));
		main.units.field[drawDirect.result.i][drawDirect.result.j].keepTurn = true;
		return this;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (framePass == framesBeforePartTwo - 1)
			main.units.field[drawDirect.result.i][drawDirect.result.j].keepTurn = false;
	}
	
	@Override
	public void onEnd()
	{
		if (isUnitDie)
			postUpdateCampaign();
	}
	
}
