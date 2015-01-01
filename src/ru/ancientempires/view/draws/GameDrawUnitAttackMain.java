package ru.ancientempires.view.draws;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.AttackResult;
import android.graphics.Canvas;

public class GameDrawUnitAttackMain extends GameDrawOnFrames
{
	
	public static final int		FRAMES_BETWEEN_ANIMATES	= 32;	// GameDrawDecreaseHealth.FRAMES_ANIMATE * 2 / 3;
																
	private GameDrawUnitAttack	drawDirect;
	private GameDrawUnitAttack	drawReverse;
	
	// private GameDrawDecreaseHealth drawDecreaseHealthDirect;
	// private GameDrawDecreaseHealth drawDecreaseHealthReverse;
	
	// private GameDrawBitmaps drawSparkBitmapsDirect;
	// private GameDrawBitmaps drawSparkBitmapsReverse;
	
	// private ArrayList<GameDrawOnFrames> draws;
	
	// private boolean attackingIsLive;
	// private boolean attackedIsLive;
	
	private boolean				isReverseAttack;
	
	// private int attackingI;
	// private int attackingJ;
	// private int attackedI;
	// private int attackedJ;
	
	public int					frameToStartPartTwo;
	
	public GameDrawUnitAttackMain(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.drawDirect = new GameDrawUnitAttack(gameDraw);
		this.drawReverse = new GameDrawUnitAttack(gameDraw);
		// this.drawDecreaseHealthDirect = new GameDrawDecreaseHealth(gameDraw);
		// this.drawDecreaseHealthReverse = new GameDrawDecreaseHealth(gameDraw);
		
		// this.drawSparkBitmapsDirect = new GameDrawBitmaps(gameDraw).setBitmaps(SparksImages.bitmapsAttack);
		// this.drawSparkBitmapsReverse = new GameDrawBitmaps(gameDraw).setBitmaps(SparksImages.bitmapsAttack);
	}
	
	public void start(ActionResult result)
	{
		// this.attackingI = (int) result.action.getProperty("i");
		// this.attackingJ = (int) result.action.getProperty("j");
		// this.attackedI = (int) result.action.getProperty("targetI");
		// this.attackedJ = (int) result.action.getProperty("targetJ");
		// int attackingY = this.attackingI * GameDraw.A;
		// int attackingX = this.attackingJ * GameDraw.A;
		// int attackedY = this.attackedI * GameDraw.A;
		// int attackedX = this.attackedJ * GameDraw.A;
		
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
		
		// this.draws = new ArrayList<GameDrawOnFrames>();
		
		// this.drawDecreaseHealthDirect.animate(0, attackedY, attackedX, resultDirect.decreaseHealth);
		// this.drawSparkBitmapsDirect.animate(0, attackedY, attackedX);
		// this.draws.add(this.drawDecreaseHealthDirect);
		// this.draws.add(this.drawSparkBitmapsDirect);
		
		// this.gameDraw.gameDrawUnit.updateOneUnitBase(this.attackedI, this.attackedJ, true);
		// this.gameDraw.inputAlgoritmMain.tapWithoutAction(this.attackedI, this.attackedJ);
		
		if (this.isReverseAttack)
		{
			// this.drawDecreaseHealthReverse.animate(GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES, attackingY, attackingX, resultReverse.decreaseHealth);
			// this.drawSparkBitmapsReverse.animate(GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES, attackingY, attackingX);
			// this.draws.add(this.drawDecreaseHealthReverse);
			// this.draws.add(this.drawSparkBitmapsReverse);
			// this.frameToStartPartTwo = this.drawDecreaseHealthReverse.frameEnd - 16 - this.gameDraw.iFrame;
		}
		
		// part 2
		
		if (resultDirect.effectSign == -1)
		{
			GameDrawOnFrames gameDrawOnFrames = new GameDrawOnFrames(this.gameDraw);
			gameDrawOnFrames.animate(this.frameToStartPartTwo, 12);
			GameDrawOnFrames gameDrawOnFrames2 = new GameDrawDecreaseHealth(this.gameDraw);
			gameDrawOnFrames2.animate(this.frameToStartPartTwo, 48);
		}
		
		animate(0, frameEnd - this.gameDraw.iFrame);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		this.drawDirect.draw(canvas);
		this.drawReverse.draw(canvas);
		
		/*
		int timePass = this.gameDraw.iFrame - this.frameStart;
		if (this.isReverseAttack && timePass == GameDrawUnitAttackMain.FRAMES_BETWEEN_ANIMATES)
		{
			this.gameDraw.inputAlgoritmMain.tapWithoutAction(this.attackingI, this.attackingJ);
			this.gameDraw.gameDrawUnit.updateOneUnitHealth(this.attackingI, this.attackingJ);
		}
		
		if (this.gameDraw.iFrame == this.frameToStartPartTwo)
		{
			this.gameDraw.gameDrawUnit.updateOneUnitBase(this.attackingI, this.attackingJ, true);
			this.gameDraw.gameDrawUnit.updateOneUnitBase(this.attackedI, this.attackedJ, true);
			this.draws.remove(this.drawDecreaseHealthDirect);
			this.draws.remove(this.drawSparkBitmapsDirect);
		}
		
		for (GameDrawOnFrames gameDrawOnFrames : this.draws)
			gameDrawOnFrames.draw(canvas);
			*/
	}
	
}
