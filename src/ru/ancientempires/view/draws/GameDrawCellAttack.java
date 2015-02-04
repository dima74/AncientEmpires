package ru.ancientempires.view.draws;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.view.draws.onframes.GameDrawBitmaps;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import android.graphics.Canvas;

public class GameDrawCellAttack extends GameDrawOnFramesGroup
{
	
	private int	targetI;
	private int	targetJ;
	private int	frameUpdateTargetCell	= -1;
	
	public GameDrawCellAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(ActionResult result, int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		int y = targetI * GameDraw.A;
		int x = targetJ * GameDraw.A;
		GameDrawOnFrames gameDraw1 = new GameDrawBitmaps(this.gameDraw).setBitmaps(SparksImages.bitmapsAttack).animate(0, y, x, 6 * 2);
		GameDrawOnFrames gameDraw2 = new GameDrawBitmaps(this.gameDraw).setBitmaps(SparksImages.bitmapsDefault).animate(6 * 2, y, x, 6 * 2 * 2);
		this.draws.add(gameDraw1);
		this.draws.add(gameDraw2);
		animate(0, gameDraw1.frameLength + gameDraw2.frameLength);
		
		this.frameUpdateTargetCell = gameDraw1.frameEnd;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		if (this.gameDraw.iFrame == this.frameUpdateTargetCell)
		{
			this.gameDraw.gameDrawCell.updateOneCell(this.gameDraw.game, this.targetI, this.targetJ);
			this.gameDraw.gameDrawCellDual.updateOneCell(this.gameDraw.game, this.targetI, this.targetJ);
		}
	}
	
}
