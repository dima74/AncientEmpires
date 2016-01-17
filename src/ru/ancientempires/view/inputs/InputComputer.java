package ru.ancientempires.view.inputs;

import java.util.ArrayList;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionGameEndTurn;
import ru.ancientempires.activity.GameActivity;

public class InputComputer extends InputBase
{
	
	@Override
	public void beginTurn()
	{
		ArrayList<Action> actions = game.ii.turn();
		ActionGameEndTurn actionGameEndTurn = (ActionGameEndTurn) actions.get(actions.size() - 1);
		actions.remove(actions.size() - 1);
		for (Action action : actions)
			action.perform();
		main.performEndTurn(actionGameEndTurn);
		
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
