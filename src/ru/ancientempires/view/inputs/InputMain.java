package ru.ancientempires.view.inputs;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.view.draws.GameDraw;

public class InputMain extends InputBase
{
	
	public InputPlayer		inputPlayer;
	public InputComputer	inputComputer;
	public InputBase		currentInput;
	
	public InputMain()
	{
		inputPlayer = new InputPlayer();
		inputComputer = new InputComputer();
		for (GameDraw gameDraw : InputBase.gameDraw.gameDraws)
			gameDraw.update();
		beginTurn();
	}
	
	@Override
	public void beginTurn()
	{
		InputBase.gameDraw.isDrawCursor = false;
		switch (InputBase.game.currentPlayer.type)
		{
			case NONE:
				endTurn();
				return;
			case PLAYER:
				currentInput = inputPlayer;
				currentInput.beginTurn();
				break;
			case COMPUTER:
				currentInput = inputComputer;
				currentInput.beginTurn();
				endTurn();
				break;
		}
		InputBase.gameDraw.gameDrawInfo.update();
	}
	
	@Override
	public void tap(int i, int j)
	{
		currentInput.tap(i, j);
	}
	
	@Override
	public void endTurn()
	{
		if (!InputBase.gameDraw.isActiveGame)
			return;
		currentInput.endTurn();
		
		Action action = new Action(ActionType.ACTION_END_TURN);
		ActionResult result = Client.action(action);
		InputBase.gameDraw.gameDrawUnitsHeal.start(result);
		
		beginTurn();
	}
	
}
