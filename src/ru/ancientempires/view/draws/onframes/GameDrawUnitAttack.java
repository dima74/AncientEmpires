package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.action.AttackResult;
import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.images.StatusesImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawUnitAttack extends GameDrawOnFramesGroup
{
	
	public AttackResult result;
	
	private int	y;
	private int	x;
	
	private int frameStartSmoke;
	
	private boolean isDirect = false;
	
	public GameDrawUnitAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawUnitAttack setDirect()
	{
		this.isDirect = true;
		return this;
	}
	
	public void initPartOne(AttackResult result)
	{
		this.result = result;
		this.y = result.targetI * GameDraw.A;
		this.x = result.targetJ * GameDraw.A;
		
		this.draws.clear();
		add(new GameDrawNumberSinus(this.gameDraw)
				.animate(this.y, this.x, -1, result.decreaseHealth));
		add(new GameDrawBitmaps(this.gameDraw)
				.setYX(this.y, this.x)
				.setBitmaps(SparksImages.bitmapsAttack)
				.animateRepeat(2));
	}
	
	public GameDrawUnitAttack initPartTwo(int framesBeforePartTwo)
	{
		if (this.result.effectSign == -1)
		{
			add(new GameDrawBitmaps(this.gameDraw)
					.setYX(this.y, this.x)
					.setBitmaps(SparksImages.bitmapsDefault)
					.animateRepeat(1)
					.increaseFrameStart(framesBeforePartTwo));
					
			int offsetY = (int) (this.y - 22 * GameDraw.a);
			int offsetX = this.x + (GameDraw.A - StatusesImages.w) / 2;
			add(new GameDrawBitmapSinus(this.gameDraw)
					.animate(offsetY, offsetX, StatusesImages.poison, 2)
					.increaseFrameStart(framesBeforePartTwo));
		}
		
		if (this.result.isLevelUp)
			add(new GameDrawLevelUp(this.gameDraw)
					.animate(this.result.i * GameDraw.A, this.result.j * GameDraw.A)
					.increaseFrameStart(framesBeforePartTwo + 4));
					
		this.gameDraw.gameDrawUnits.field[this.result.targetI][this.result.targetJ].canUpdateHealth = false;
		if (!this.result.isTargetLive)
		{
			this.gameDraw.gameDrawUnitsDead.keep[this.result.targetI][this.result.targetJ] = true;
			this.gameDraw.gameDrawUnits.keep[this.result.targetI][this.result.targetJ] = true;
			
			GameDrawOnFrames gameDrawBitmaps = new GameDrawBitmaps(this.gameDraw)
					.setYX(this.y, this.x)
					.setBitmaps(SparksImages.bitmapsDefault)
					.animateRepeat(1)
					.increaseFrameStart(framesBeforePartTwo);
			add(gameDrawBitmaps);
			this.frameStartSmoke = gameDrawBitmaps.frameEnd;
			
			int startY = this.y;
			int startX = this.x;
			int endY = startY - 3 * 2 * SmokeImages.amountDefault;
			int endX = startX;
			add(new GameDrawBitmapsMoving(this.gameDraw)
					.setLineYX(startY, startX, endY, endX)
					.setBitmaps(SmokeImages.bitmapsDefault)
					.setFramesForBitmap(4)
					.animateRepeat(1)
					.increaseFrameStart(framesBeforePartTwo + gameDrawBitmaps.frameCount));
		}
		return this;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		super.drawOnFrames(canvas);
		if (this.gameDraw.iFrame == this.frameStart)
		{
			this.gameDraw.gameDrawUnits.field[this.result.targetI][this.result.targetJ].canUpdateHealth = true;
			if (this.result.i == this.gameDraw.inputAlgorithmMain.lastTapI && this.result.j == this.gameDraw.inputAlgorithmMain.lastTapJ)
				this.gameDraw.inputAlgorithmMain.tapWithoutAction(this.result.targetI, this.result.targetJ);
		}
		if (this.gameDraw.iFrame == this.frameStartSmoke - 1)
		{
			this.gameDraw.gameDrawUnitsDead.keep[this.result.targetI][this.result.targetJ] = false;
			// this.gameDraw.gameDrawUnits.updateOneUnit(this.result.targetI, this.result.targetJ);
			this.gameDraw.gameDrawUnits.keep[this.result.targetI][this.result.targetJ] = false;
		}
	}
	
}
