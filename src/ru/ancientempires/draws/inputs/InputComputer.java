package ru.ancientempires.draws.inputs;

import java.util.ArrayList;

import ru.ancientempires.GameThread;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionGameEndTurn;

public class InputComputer extends AbstractPlayerInput
{
	
	@Override
	public void beginTurn()
	{
		ArrayList<Action> actions = game.ii.turn(game);
		ActionGameEndTurn actionGameEndTurn = (ActionGameEndTurn) actions.get(actions.size() - 1);
		actions.remove(actions.size() - 1);
		for (Action action : actions)
			action.perform(game);
		inputMain.performEndTurn(actionGameEndTurn);
		
		drawMain.units.update();
		drawMain.cells.update();
		drawMain.cellsDual.update();
		drawMain.info.update();
		GameThread.thread.needUpdateCampaign = true;
	}
	
	@Override
	public void tap(int i, int j)
	{}
	
	@Override
	public void endTurn()
	{}
	
}
