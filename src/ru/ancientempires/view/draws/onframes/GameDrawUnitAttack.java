package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.action.AttackResult;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawUnitAttack extends GameDrawOnFramesGroup
{
	
	public AttackResult result;
	
	private int	y;
	private int	x;
	
	private int frameStartSmoke;
	
	private boolean isDirect = false;
	
	public GameDrawUnitAttack setDirect()
	{
		isDirect = true;
		return this;
	}
	
	public void initPartOne(AttackResult result)
	{
		this.result = result;
		y = result.targetI * GameDraw.A;
		x = result.targetJ * GameDraw.A;
		
		draws.clear();
		add(new GameDrawNumberSinus()
				.animate(y, x, -1, result.decreaseHealth));
		add(new GameDrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(2));
	}
	
	public GameDrawUnitAttack initPartTwo(int framesBeforePartTwo)
	{
		if (result.effectSign == -1)
		{
			add(new GameDrawBitmaps()
					.setYX(y, x)
					.setBitmaps(SparksImages().bitmapsDefault)
					.animateRepeat(1)
					.increaseFrameStart(framesBeforePartTwo));
					
			int offsetY = (int) (y - 22 * GameDraw.a);
			int offsetX = x + (GameDraw.A - StatusesImages().w) / 2;
			add(new GameDrawBitmapSinus()
					.animate(offsetY, offsetX, StatusesImages().poison, 2)
					.increaseFrameStart(framesBeforePartTwo));
		}
		
		if (result.isLevelUp)
			add(new GameDrawLevelUp()
					.animate(result.i * GameDraw.A, result.j * GameDraw.A)
					.increaseFrameStart(framesBeforePartTwo + 4));
					
		GameDraw.main.gameDrawUnits.field[result.targetI][result.targetJ].canUpdateHealth = false;
		if (!result.isTargetLive)
		{
			GameDraw.main.gameDrawUnitsDead.keep[result.targetI][result.targetJ] = true;
			GameDraw.main.gameDrawUnits.keep[result.targetI][result.targetJ] = true;
			
			GameDrawOnFrames gameDrawBitmaps = new GameDrawBitmaps()
					.setYX(y, x)
					.setBitmaps(SparksImages().bitmapsDefault)
					.animateRepeat(1)
					.increaseFrameStart(framesBeforePartTwo);
			add(gameDrawBitmaps);
			frameStartSmoke = gameDrawBitmaps.frameEnd;
			
			int startY = y;
			int startX = x;
			int endY = startY - 3 * 2 * SmokeImages().amountDefault;
			int endX = startX;
			add(new GameDrawBitmapsMoving()
					.setLineYX(startY, startX, endY, endX)
					.setBitmaps(SmokeImages().bitmapsDefault)
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
		if (GameDraw.iFrame == frameStart)
		{
			GameDraw.main.gameDrawUnits.field[result.targetI][result.targetJ].canUpdateHealth = true;
			if (result.i == GameDraw.main.inputPlayer.lastTapI && result.j == GameDraw.main.inputPlayer.lastTapJ)
				GameDraw.main.inputPlayer.tapWithoutAction(result.targetI, result.targetJ);
		}
		if (GameDraw.iFrame == frameStartSmoke - 1)
		{
			GameDraw.main.gameDrawUnitsDead.keep[result.targetI][result.targetJ] = false;
			GameDraw.main.gameDrawUnits.keep[result.targetI][result.targetJ] = false;
			GameDraw.main.gameDrawInfo.update();
		}
	}
	
}
