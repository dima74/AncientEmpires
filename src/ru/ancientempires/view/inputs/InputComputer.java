package ru.ancientempires.view.inputs;

import ru.ancientempires.activity.GameActivity;

public class InputComputer extends InputBase
{
	
	@Override
	public void beginTurn()
	{
		game.ii.turn();
		InputBase.gameDraw.gameDrawUnits.update();
		InputBase.gameDraw.gameDrawCells.update();
		InputBase.gameDraw.gameDrawCellDual.update();
		InputBase.gameDraw.gameDrawInfo.update();
		GameActivity.activity.view.thread.needUpdateCampaign = true;
	}
	
	@Override
	public void tap(int i, int j)
	{}
	
	@Override
	public void endTurn()
	{}
	
}
