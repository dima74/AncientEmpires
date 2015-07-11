package ru.ancientempires.view.draws.onframes;

import java.util.ArrayList;

import ru.ancientempires.action.AttackResult;
import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.images.StatusesImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawUnitAttack extends GameDrawOnFramesGroup
{
	
	private AttackResult	result;
	
	private int				y;
	private int				x;
	
	private int				frameStartPartTwo;
	private int				frameStartSmoke;
	private boolean			isDirect;
	
	public GameDrawUnitAttack(GameDrawMain gameDraw, boolean isDirect)
	{
		super(gameDraw);
		this.isDirect = isDirect;
	}
	
	public void start(AttackResult result, int frameToStart)
	{
		this.result = result;
		this.y = result.targetI * GameDraw.A;
		this.x = result.targetJ * GameDraw.A;
		
		this.draws = new ArrayList<GameDrawOnFrames>();
		GameDrawDecreaseHealth drawDecreaseHealth = new GameDrawDecreaseHealth(this.gameDraw);
		GameDrawBitmaps drawSparkBitmaps = new GameDrawBitmaps(this.gameDraw)
				.setYX(this.y, this.x)
				.setBitmaps(SparksImages.bitmapsAttack)
				.animateRepeat(frameToStart, 2);
		
		drawDecreaseHealth.animate(frameToStart, this.y, this.x, -1, result.decreaseHealth);
		
		this.draws.add(drawDecreaseHealth);
		this.draws.add(drawSparkBitmaps);
		
		animate(frameToStart, GameDrawDecreaseHealth.FRAMES_ANIMATE);
	}
	
	public void setFrameToStartPartTwo(int frameToStartPartTwo)
	{
		this.frameStartPartTwo = this.gameDraw.iFrame + frameToStartPartTwo;
		this.frameEnd = Math.max(this.frameEnd, this.frameStartPartTwo);
		
		if (this.result.effectSign == -1)
		{
			GameDrawBitmaps drawSparks = new GameDrawBitmaps(this.gameDraw)
					.setYX(this.y, this.x)
					.setBitmaps(SparksImages.bitmapsDefault)
					.animateRepeat(frameToStartPartTwo, 1);
			
			int offsetY = (int) (this.y - 22 * GameDraw.a);
			int offsetX = this.x + (GameDraw.A - StatusesImages.w) / 2;
			GameDrawOnFrames drawPoison = new GameDrawBitmapSinus(this.gameDraw)
					.setBitmap(StatusesImages.poison)
					.setCoord(offsetY, offsetX)
					.animate(frameToStartPartTwo, 48);
			
			this.draws.add(drawSparks);
			this.draws.add(drawPoison);
			
			this.frameEnd = Math.max(this.frameEnd, drawSparks.frameEnd);
			this.frameEnd = Math.max(this.frameEnd, drawPoison.frameEnd);
		}
		
		if (this.result.isLevelUp)
		{
			int frameToStartLevelUp = frameToStartPartTwo + 4;
			GameDrawLevelUp drawLevelUp = new GameDrawLevelUp(this.gameDraw);
			drawLevelUp.animate(frameToStartLevelUp, this.result.i * GameDraw.A, this.result.j * GameDraw.A);
			this.draws.add(drawLevelUp);
			
			this.frameEnd = Math.max(this.frameEnd, drawLevelUp.frameEnd);
		}
		
		if (!this.result.isTargetLive)
		{
			GameDrawOnFrames gameDrawBitmaps = new GameDrawBitmaps(this.gameDraw)
					.setYX(this.y, this.x)
					.setBitmaps(SparksImages.bitmapsDefault)
					.animateRepeat(frameToStartPartTwo, 1);
			this.draws.add(gameDrawBitmaps);
			this.frameStartSmoke = gameDrawBitmaps.frameEnd;
			
			int startY = this.y;
			int startX = this.x;
			int endY = startY - 3 * 2 * SmokeImages.amountDefault;
			int endX = startX;
			GameDrawOnFrames gameDrawBitmapsMoving = new GameDrawBitmapsMoving(this.gameDraw)
					.setLineYX(startY, startX, endY, endX)
					.setBitmaps(SmokeImages.bitmapsDefault)
					.setFramesForBitmap(4)
					.animateRepeat(this.frameStartSmoke - this.gameDraw.iFrame, 1);
			this.draws.add(gameDrawBitmapsMoving);
			
			this.frameEnd = Math.max(this.frameEnd, gameDrawBitmapsMoving.frameEnd);
		}
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		if (this.gameDraw.iFrame == this.frameStart)
		{
			this.gameDraw.gameDrawUnit.updateOneUnitHealth(this.result.targetI, this.result.targetJ, this.result.isTargetLive);
			if (this.result.i == this.gameDraw.inputAlgorithmMain.lastTapI && this.result.j == this.gameDraw.inputAlgorithmMain.lastTapJ)
				this.gameDraw.inputAlgorithmMain.tapWithoutAction(this.result.targetI, this.result.targetJ);
		}
		if (this.gameDraw.iFrame == this.frameStartPartTwo - 1 && this.isDirect)
			this.gameDraw.gameDrawUnit.updateOneUnitBase(this.result.i, this.result.j, true);
		if (this.gameDraw.iFrame == this.frameStartSmoke - 1)
			this.gameDraw.gameDrawUnit.updateOneUnit(this.result.targetI, this.result.targetJ);
	}
	
}
