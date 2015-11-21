package ru.ancientempires.view.inputs;

import ru.ancientempires.ii.II;

public class InputComputer extends InputBase
{
	
	@Override
	public void beginTurn()
	{
		II.ii.turn();
		InputBase.gameDraw.gameDrawUnits.update();
		InputBase.gameDraw.gameDrawCells.update();
		InputBase.gameDraw.gameDrawCellDual.update();
		InputBase.gameDraw.gameDrawInfo.update();
		InputBase.thread.needUpdateCampaign = true;
	}
	
	@Override
	public void tap(int i, int j)
	{}
	
	@Override
	public void endTurn()
	{}
	
}
