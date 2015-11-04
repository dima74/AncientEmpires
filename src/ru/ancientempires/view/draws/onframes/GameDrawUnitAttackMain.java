package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.AttackResult;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawUnitAttackMain extends GameDrawOnFramesGroup
{
	
	public static final int FRAMES_BETWEEN_ANIMATES = 32; // GameDrawDecreaseHealth.FRAMES_ANIMATE * 2 / 3;
	
	private GameDrawUnitAttack	drawDirect;
	private GameDrawUnitAttack	drawReverse;
	
	private boolean	isReverseAttack;
	private boolean	isUnitDie;
	
	public int	frameToStartPartTwo;
	public int	framesBeforePartTwo;
	
	public GameDrawUnitAttackMain(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.drawDirect = new GameDrawUnitAttack(gameDraw).setDirect();
		this.drawReverse = new GameDrawUnitAttack(gameDraw);
	}
	
	public void start(ActionResult result)
	{
		AttackResult resultDirect = (AttackResult) result.getProperty("attackResultDirect");
		this.isUnitDie = !resultDirect.isTargetLive;
		
		this.drawDirect.initPartOne(resultDirect);
		this.framesBeforePartTwo = this.drawDirect.frameCount - 16;
		
		this.isReverseAttack = (boolean) result.getProperty("isReverseAttack");
		if (this.isReverseAttack)
		{
			AttackResult resultReverse = (AttackResult) result.getProperty("attackResultReverse");
			this.drawReverse.initPartOne(resultReverse);
			this.drawReverse.increaseFrameStart(GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES);
			this.framesBeforePartTwo = GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES + this.drawReverse.frameCount - 16;
			this.isUnitDie |= !resultReverse.isTargetLive;
		}
		
		this.draws.clear();
		add(this.drawDirect.initPartTwo(this.framesBeforePartTwo));
		if (this.isReverseAttack)
			add(this.drawReverse.initPartTwo(this.framesBeforePartTwo));
		this.gameDraw.gameDrawUnits.field[this.drawDirect.result.i][this.drawDirect.result.j].keepTurn = true;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (this.framePass == this.framesBeforePartTwo - 1)
			this.gameDraw.gameDrawUnits.field[this.drawDirect.result.i][this.drawDirect.result.j].keepTurn = false;
		if (this.gameDraw.iFrame == this.frameEnd)
		{
			this.gameDraw.gameDrawInfo.update(this.gameDraw.game);
			if (this.isUnitDie)
				GameActivity.gameView.thread.needUpdateCampaign = true;
		}
	}
	
}
