package ru.ancientempires.view.draws;

import ru.ancientempires.action.ActionResult;
import android.graphics.Canvas;

public class GameDrawCellAttack extends GameDraw
{
	
	public GameDrawCellAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(ActionResult result, int endI, int endJ)
	{
		this.gameDraw.gameDrawCell.updateOneCell(this.gameDraw.game, endI, endJ);
		// this.gameDraw.gameDrawCellDual.updateOneCell(this.gameDraw.game, endI, endJ);
	}
	
	@Override
	public void draw(Canvas canvas)
	{	
		
	}
	
}
