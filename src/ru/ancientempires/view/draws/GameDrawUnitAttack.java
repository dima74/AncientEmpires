package ru.ancientempires.view.draws;

import java.util.ArrayList;

import ru.ancientempires.action.AttackResult;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.images.StatusesImages;
import android.graphics.Canvas;

public class GameDrawUnitAttack extends GameDrawOnFrames
{
	
	private AttackResult				result;
	
	private int							i, j;
	private int							y, x;
	
	private ArrayList<GameDrawOnFrames>	draws;
	
	private int							frameStartPartTwo;
	
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
			GameDrawBitmaps gameDrawSparks = new GameDrawBitmaps(this.gameDraw).setBitmaps(SparksImages.bitmapsDefault);
			
			int offsetY = (int) (this.y - 22 * GameDraw.a);
			int offsetX = this.x + (GameDraw.A - StatusesImages.w) / 2;
			GameDrawOnFrames gameDrawPoison = new GameDrawBitmapSinus(this.gameDraw).setBitmap(StatusesImages.poison).setCoord(offsetY, offsetX);
			
			gameDrawSparks.animate(frameToStartPartTwo, this.y, this.x, GameDrawBitmaps.FRAMES_ANIMATE_SHORT);
			gameDrawPoison.animate(frameToStartPartTwo, 48);
			
			this.draws.add(gameDrawSparks);
			this.draws.add(gameDrawPoison);
			
			this.frameEnd = Math.max(this.frameEnd, gameDrawSparks.frameEnd);
			this.frameEnd = Math.max(this.frameEnd, gameDrawPoison.frameEnd);
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
			this.gameDraw.gameDrawUnit.updateOneUnitHealth(this.i, this.j);
			this.gameDraw.inputAlgoritmMain.tapWithoutAction(this.i, this.j);
		}
		if (this.gameDraw.iFrame == this.frameStartPartTwo)
			this.gameDraw.gameDrawUnit.updateOneUnitBase(this.i, this.j, true);
		
		for (GameDrawOnFrames gameDrawOnFrames : this.draws)
			gameDrawOnFrames.draw(canvas);
	}
	
}
