package ru.ancientempires.draws.inputs;

import java.util.ArrayList;

import ru.ancientempires.actions.Action;
import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.actions.campaign.ActionCampaignEnterCampaign;
import ru.ancientempires.actions.campaign.ActionCampaignLeaveCampaign;

public class InputComputer extends AbstractPlayerInput
{

	public InputComputer(InputMain inputMain)
	{
		super(inputMain);
	}

	@Override
	public void beginTurn()
	{
		new ActionCampaignEnterCampaign().perform(game);

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
		drawMain.postUpdateCampaign();

		new ActionCampaignLeaveCampaign().perform(game);
	}

	@Override
	public void tap(int i, int j)
	{}

	@Override
	public void endTurn()
	{}

}
