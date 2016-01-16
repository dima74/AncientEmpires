package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.action.result.AttackResult;

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
		y = result.targetI * A;
		x = result.targetJ * A;
		
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
					
			int offsetY = (int) (y - 22 * a);
			int offsetX = x + (A - StatusesImages().w) / 2;
			add(new GameDrawBitmapSinus()
					.animate(offsetY, offsetX, StatusesImages().poison, 2)
					.increaseFrameStart(framesBeforePartTwo));
		}
		
		if (result.isLevelUp)
			add(new GameDrawLevelUp()
					.animate(result.i * A, result.j * A)
					.increaseFrameStart(framesBeforePartTwo + 4));
					
		main.gameDrawUnits.field[result.targetI][result.targetJ].canUpdateHealth = false;
		if (!result.isTargetLive)
		{
			main.gameDrawUnitsDead.keep[result.targetI][result.targetJ] = true;
			main.gameDrawUnits.keep[result.targetI][result.targetJ] = true;
			
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
		if (iFrame() == frameStart)
		{
			main.gameDrawUnits.field[result.targetI][result.targetJ].canUpdateHealth = true;
			if (result.i == main.inputPlayer.lastTapI && result.j == main.inputPlayer.lastTapJ)
				main.inputPlayer.tapWithoutAction(result.targetI, result.targetJ);
		}
		if (iFrame() == frameStartSmoke - 1)
		{
			main.gameDrawUnitsDead.keep[result.targetI][result.targetJ] = false;
			main.gameDrawUnits.keep[result.targetI][result.targetJ] = false;
			main.gameDrawInfo.update();
		}
	}
	
}
