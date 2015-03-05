package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.AttackResult;
import ru.ancientempires.model.Unit;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawUnitAttackMain extends GameDrawOnFrames
{
	
	public static final int		FRAMES_BETWEEN_ANIMATES	= 32;	// GameDrawDecreaseHealth.FRAMES_ANIMATE * 2 / 3;
																
	private GameDrawUnitAttack	drawDirect;
	private GameDrawUnitAttack	drawReverse;
	
	private boolean				isReverseAttack;
	
	public int					frameToStartPartTwo;
	
	public GameDrawUnitAttackMain(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.drawDirect = new GameDrawUnitAttack(gameDraw, true);
		this.drawReverse = new GameDrawUnitAttack(gameDraw, false);
	}
	
	public void start(ActionResult result)
	{
		AttackResult resultDirect = (AttackResult) result.getProperty("attackResultDirect");
		this.drawDirect.start(resultDirect, 0);
		int frameToStartPartTwo = this.drawDirect.frameEnd - this.gameDraw.iFrame - 16;
		
		this.isReverseAttack = (boolean) result.getProperty("isReverseAttack");
		if (this.isReverseAttack)
		{
			AttackResult resultReverse = (AttackResult) result.getProperty("attackResultReverse");
			this.drawReverse.start(resultReverse, GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES);
			frameToStartPartTwo = this.drawReverse.frameEnd - this.gameDraw.iFrame - 16;
		}
		
		this.drawDirect.setFrameToStartPartTwo(frameToStartPartTwo);
		int frameEnd = this.drawDirect.frameEnd;
		if (this.isReverseAttack)
		{
			this.drawReverse.setFrameToStartPartTwo(frameToStartPartTwo);
			frameEnd = Math.max(frameEnd, this.drawReverse.frameEnd);
		}
		
		Unit[] unitsToUpdate = (Unit[]) result.getProperty("unitsToUpdate");
		for (Unit unit : unitsToUpdate)
			this.gameDraw.gameDrawUnit.updateOneUnit(unit.i, unit.j);
		
		animate(0, frameEnd - this.gameDraw.iFrame);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (this.gameDraw.iFrame == this.frameEnd)
			this.gameDraw.gameDrawInfo.update(this.gameDraw.game);
		if (!this.isDrawing)
			return;
		
		this.drawDirect.draw(canvas);
		this.drawReverse.draw(canvas);
	}
	
}
