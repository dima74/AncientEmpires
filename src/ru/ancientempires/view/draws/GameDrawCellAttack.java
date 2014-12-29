package ru.ancientempires.view.draws;

import ru.ancientempires.action.ActionResult;
import android.graphics.Canvas;

public class GameDrawCellAttack extends GameDraw
{
	
	public GameDrawCellAttack(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void start(ActionResult result)
	{
		this.gameDraw.gameDrawCell.update(this.gameDraw.game);
		this.gameDraw.gameDrawCellDual.update(this.gameDraw.game);
	}
	
	@Override
	public void draw(Canvas canvas)
	{	
		
	}
	
}
