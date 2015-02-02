package ru.ancientempires.view.draws.onframes;

import java.util.ArrayList;

import ru.ancientempires.action.AttackResult;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.images.StatusesImages;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawUnitAttack extends GameDrawOnFramesGroup
{
	
	private AttackResult	result;
	
	private int				i, j;
	private int				y, x;
	
	private int				frameStartPartTwo;
	
	public GameDrawUnitAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(AttackResult result, int frameToStart)
	{
		this.result = result;
		this.y = (this.i = result.targetI) * GameDraw.A;
		this.x = (this.j = result.targetJ) * GameDraw.A;
		
		this.draws = new ArrayList<GameDrawOnFrames>();
		GameDrawDecreaseHealth drawDecreaseHealth = new GameDrawDecreaseHealth(this.gameDraw);
		GameDrawBitmaps drawSparkBitmaps = new GameDrawBitmaps(this.gameDraw).setBitmaps(SparksImages.bitmapsAttack);
		
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
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		if (this.gameDraw.iFrame == this.frameStart)
		{
			this.gameDraw.gameDrawUnit.updateOneUnitHealth(this.i, this.j, this.result.isTargetLive);
			if (this.result.i == this.gameDraw.inputAlgoritmMain.lastTapI && this.result.j == this.gameDraw.inputAlgoritmMain.lastTapJ)
				this.gameDraw.inputAlgoritmMain.tapWithoutAction(this.i, this.j);
		}
		if (this.gameDraw.iFrame == this.frameStartPartTwo)
			this.gameDraw.gameDrawUnit.updateOneUnitBaseIfExist(this.result.i, this.result.j, true);
	}
	
}
