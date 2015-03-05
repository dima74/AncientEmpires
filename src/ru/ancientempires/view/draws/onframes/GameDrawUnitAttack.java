package ru.ancientempires.view.draws.onframes;

import java.util.ArrayList;

import ru.ancientempires.action.AttackResult;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.images.StatusesImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawUnitAttack extends GameDrawOnFramesGroup
{
	
	private Bitmap[]		bitmaps;
	private AttackResult	result;
	
	private int				y, x;
	
	private int				frameStartPartTwo;
	private int				frameStartSmoke;
	private boolean			isDirect;
	
	public GameDrawUnitAttack(GameDrawMain gameDraw, boolean isDirect)
	{
		super(gameDraw);
		this.isDirect = isDirect;
		
		int amount = SparksImages.amountAttack;
		this.bitmaps = new Bitmap[amount * 2];
		for (int i = 0; i < amount; i++)
			this.bitmaps[i] = this.bitmaps[i + amount] = SparksImages.bitmapsAttack[i];
	}
	
	public void start(AttackResult result, int frameToStart)
	{
		this.result = result;
		this.y = result.targetI * GameDraw.A;
		this.x = result.targetJ * GameDraw.A;
		
		this.draws = new ArrayList<GameDrawOnFrames>();
		GameDrawDecreaseHealth drawDecreaseHealth = new GameDrawDecreaseHealth(this.gameDraw);
		GameDrawBitmaps drawSparkBitmaps = new GameDrawBitmaps(this.gameDraw).setBitmaps(this.bitmaps);
		
		drawDecreaseHealth.animate(frameToStart, this.y, this.x, result.decreaseHealth);
		drawSparkBitmaps.animate(frameToStart, this.y, this.x, GameDrawBitmaps.FRAMES_ANIMATE_LONG);
		
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
			GameDrawBitmaps drawSparks = new GameDrawBitmaps(this.gameDraw).setBitmaps(SparksImages.bitmapsDefault);
			
			int offsetY = (int) (this.y - 22 * GameDraw.a);
			int offsetX = this.x + (GameDraw.A - StatusesImages.w) / 2;
			GameDrawOnFrames drawPoison = new GameDrawBitmapSinus(this.gameDraw).setBitmap(StatusesImages.poison).setCoord(offsetY, offsetX);
			
			drawSparks.animate(frameToStartPartTwo, this.y, this.x, GameDrawBitmaps.FRAMES_ANIMATE_SHORT);
			drawPoison.animate(frameToStartPartTwo, 48);
			
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
			GameDrawOnFrames gameDraw = new GameDrawBitmaps(this.gameDraw)
					.setBitmaps(SparksImages.bitmapsDefault)
					.animate(frameToStartPartTwo, this.y, this.x, SparksImages.amountDefault * 2);
			this.draws.add(gameDraw);
			this.frameStartSmoke = gameDraw.frameEnd;
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
