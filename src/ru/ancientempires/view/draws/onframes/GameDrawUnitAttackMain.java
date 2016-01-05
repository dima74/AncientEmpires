package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.AttackResult;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawUnitAttackMain extends GameDrawOnFramesGroup
{
	
	public static final int FRAMES_BETWEEN_ANIMATES = 32; // GameDrawDecreaseHealth.FRAMES_ANIMATE * 2 / 3;
	
	private GameDrawUnitAttack	drawDirect;
	private GameDrawUnitAttack	drawReverse;
	
	private boolean	isReverseAttack;
	private boolean	isUnitDie;
	
	public int	frameToStartPartTwo;
	public int	framesBeforePartTwo;
	
	public GameDrawUnitAttackMain()
	{
		drawDirect = new GameDrawUnitAttack().setDirect();
		drawReverse = new GameDrawUnitAttack();
	}
	
	public GameDrawUnitAttackMain start(ActionResult result)
	{
		AttackResult resultDirect = (AttackResult) result.getProperty("attackResultDirect");
		isUnitDie = !resultDirect.isTargetLive;
		
		drawDirect.initPartOne(resultDirect);
		framesBeforePartTwo = drawDirect.frameCount - 16;
		
		isReverseAttack = (boolean) result.getProperty("isReverseAttack");
		if (isReverseAttack)
		{
			AttackResult resultReverse = (AttackResult) result.getProperty("attackResultReverse");
			drawReverse.initPartOne(resultReverse);
			drawReverse.increaseFrameStart(GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES);
			framesBeforePartTwo = GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES + drawReverse.frameCount - 16;
			isUnitDie |= !resultReverse.isTargetLive;
		}
		
		draws.clear();
		add(drawDirect.initPartTwo(framesBeforePartTwo));
		if (isReverseAttack)
			add(drawReverse.initPartTwo(framesBeforePartTwo));
		GameDraw.main.gameDrawUnits.field[drawDirect.result.i][drawDirect.result.j].keepTurn = true;
		return this;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (framePass == framesBeforePartTwo - 1)
			GameDraw.main.gameDrawUnits.field[drawDirect.result.i][drawDirect.result.j].keepTurn = false;
		if (GameDraw.iFrame == frameEnd)
			if (isUnitDie)
				GameActivity.activity.view.thread.needUpdateCampaign = true;
	}
	
}
