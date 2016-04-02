package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.Strings;
import ru.ancientempires.action.result.AttackResult;

public class DrawUnitAttack extends DrawOnFramesGroup
{
	
	public AttackResult	result;
						
	private int			y;
	private int			x;
						
	private int			frameStartSmoke;
	private int			frameUpdateBonus;
						
	private boolean		isDirect	= false;
									
	public DrawUnitAttack setDirect()
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
		add(new DrawNumberSinus()
				.animate(y, x, -1, result.decreaseHealth));
		add(new DrawBitmaps()
				.setYX(y, x)
				.setBitmaps(SparksImages().bitmapsAttack)
				.animateRepeat(2));
	}
	
	public DrawUnitAttack initPartTwo(int framesBeforePartTwo)
	{
		if (result.effectSign == -1)
		{
			add(new DrawBitmaps()
					.setYX(y, x)
					.setBitmaps(SparksImages().bitmapsDefault)
					.animateRepeat(1)
					.increaseFrameStart(framesBeforePartTwo));
					
			int offsetY = (int) (y - 22 * a);
			int offsetX = x + (A - StatusesImages().w) / 2;
			DrawOnFrames draw = new DrawBitmapSinus()
					.animate(offsetY, offsetX, StatusesImages().poison, 2)
					.increaseFrameStart(framesBeforePartTwo);
			add(draw);
			main.units.field[result.targetI][result.targetJ].canUpdateNegativeBonus = false;
			frameUpdateBonus = draw.frameEnd;
		}
		
		if (result.isLevelUp)
		{
			DrawOnFrames levelUp = new DrawLevelUp()
					.animate(result.i * A, result.j * A)
					.increaseFrameStart(framesBeforePartTwo + 4);
			add(levelUp);
			if (result.isPromotion)
				add(new DrawToast(String.format(Strings.PROMOTION.toString(), game.fieldUnits[result.i][result.j].name))
						.setFrameStart(levelUp.frameEnd + 1));
		}
		
		main.units.field[result.targetI][result.targetJ].canUpdateHealth = false;
		if (!result.isTargetLive)
		{
			main.unitsDead.keep[result.targetI][result.targetJ] = true;
			main.units.keep[result.targetI][result.targetJ] = true;
			
			DrawOnFrames gameDrawBitmaps = new DrawBitmaps()
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
			add(new DrawBitmapsMoving()
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
			main.units.field[result.targetI][result.targetJ].canUpdateHealth = true;
			if (result.i == main.inputPlayer.lastTapI && result.j == main.inputPlayer.lastTapJ)
				main.inputPlayer.tapWithoutAction(result.targetI, result.targetJ);
		}
		if (iFrame() == frameStartSmoke - 1)
		{
			main.unitsDead.keep[result.targetI][result.targetJ] = false;
			main.units.keep[result.targetI][result.targetJ] = false;
			main.info.update();
		}
		if (iFrame() == frameUpdateBonus)
			main.units.field[result.targetI][result.targetJ].canUpdateNegativeBonus = true;
	}
	
}
