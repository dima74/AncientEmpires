package ru.ancientempires.view.draws;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.images.SparksImages;
import android.graphics.Canvas;

public class GameDrawUnitAttack extends GameDraw
{
	
	private static final int		FRAMES_BETWEEN_ANIMATES	= GameDrawDecreaseHealth.FRAMES_ANIMATE * 2 / 3;
	
	private GameDrawDecreaseHealth	drawDecreaseHealthDirect;
	private GameDrawDecreaseHealth	drawDecreaseHealthReverse;
	
	private GameDrawBitmaps			drawSparkBitmapsDirect;
	private GameDrawBitmaps			drawSparkBitmapsReverse;
	
	private boolean					attackingIsLive;
	private boolean					attackedIsLive;
	
	private boolean					isReverseDrawing;
	private boolean					isDrawing				= false;
	private int						frameStart;
	
	private int						attackingI;
	private int						attackingJ;
	private int						attackedI;
	private int						attackedJ;
	
	public GameDrawUnitAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.drawDecreaseHealthDirect = new GameDrawDecreaseHealth(gameDraw);
		this.drawDecreaseHealthReverse = new GameDrawDecreaseHealth(gameDraw);
		
		this.drawSparkBitmapsDirect = new GameDrawBitmaps(gameDraw).setBitmaps(SparksImages.attackBitmaps);
		this.drawSparkBitmapsReverse = new GameDrawBitmaps(gameDraw).setBitmaps(SparksImages.attackBitmaps);
	}
	
	public void start(ActionResult result)
	{
		this.attackingI = (int) result.action.getProperty("i");
		this.attackingJ = (int) result.action.getProperty("j");
		this.attackedI = (int) result.action.getProperty("targetI");
		this.attackedJ = (int) result.action.getProperty("targetJ");
		int attackingY = this.attackingI * GameDraw.A;
		int attackingX = this.attackingJ * GameDraw.A;
		int attackedY = this.attackedI * GameDraw.A;
		int attackedX = this.attackedJ * GameDraw.A;
		
		int attackingDecrease = (int) result.getProperty("attackingDecrease");
		int attackedDecrease = (int) result.getProperty("attackedDecrease");
		boolean attackingIsLive = (boolean) result.getProperty("attackingLive");
		boolean attackedIsLive = (boolean) result.getProperty("attackedLive");
		
		this.drawDecreaseHealthDirect.initAnimate(attackedY, attackedX, attackedDecrease);
		this.drawSparkBitmapsDirect.initAnimate(attackedY, attackedX);
		this.drawDecreaseHealthDirect.startAnimate();
		this.drawSparkBitmapsDirect.startAnimate();
		this.gameDraw.gameDrawUnit.updateOneUnit(this.attackedI, this.attackedJ);
		
		this.gameDraw.inputAlgoritmMain.tapWithoutAction(this.attackedI, this.attackedJ);
		if (this.isReverseDrawing = attackingDecrease >= 0)
		{
			this.drawDecreaseHealthReverse.initAnimate(attackingY, attackingX, attackingDecrease);
			this.drawSparkBitmapsReverse.initAnimate(attackingY, attackingX);
		}
		
		this.isDrawing = true;
		this.frameStart = this.gameDraw.iFrame;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (!this.isDrawing)
			return;
		
		int timePass = this.gameDraw.iFrame - this.frameStart;
		if (timePass == GameDrawUnitAttack.FRAMES_BETWEEN_ANIMATES && this.isReverseDrawing)
		{
			this.gameDraw.inputAlgoritmMain.tapWithoutAction(this.attackingI, this.attackingJ);
			this.drawDecreaseHealthReverse.startAnimate();
			this.drawSparkBitmapsReverse.startAnimate();
			this.gameDraw.gameDrawUnit.updateOneUnit(this.attackingI, this.attackingJ);
		}
		
		this.drawDecreaseHealthDirect.draw(canvas);
		this.drawDecreaseHealthReverse.draw(canvas);
		
		this.drawSparkBitmapsDirect.draw(canvas);
		this.drawSparkBitmapsReverse.draw(canvas);
		
		this.isDrawing = this.drawDecreaseHealthDirect.isDrawing || this.drawDecreaseHealthReverse.isDrawing;
		if (!this.isDrawing)
			this.gameDraw.gameDrawUnit.update(this.gameDraw.game);
	}
	
}
