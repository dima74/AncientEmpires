package ru.ancientempires.view.inputs;

import ru.ancientempires.action.ActionGameEndTurn;
import ru.ancientempires.action.result.ActionResultGameEndTurn;
import ru.ancientempires.handler.IGameHandler;
import ru.ancientempires.view.draws.GameDraw;

public class InputMain extends IGameHandler
{
	
	public InputPlayer		inputPlayer;
	public InputComputer	inputComputer;
	public InputBase		currentInput;
	
	public InputMain()
	{
		inputPlayer = new InputPlayer();
		inputComputer = new InputComputer();
		inputPlayer.main = this;
		inputComputer.main = this;
		for (GameDraw gameDraw : InputBase.gameDraw.gameDraws)
			gameDraw.update();
		beginTurn();
	}
	
	public void beginTurn()
	{
		InputBase.gameDraw.isDrawCursor = false;
		switch (game.currentPlayer.type)
		{
			case NONE:
				endTurn(true);
				return;
			case PLAYER:
				currentInput = inputPlayer;
				currentInput.beginTurn();
				break;
			case COMPUTER:
				currentInput = inputComputer;
				currentInput.beginTurn();
				endTurn(false);
				break;
		}
		InputBase.gameDraw.gameDrawInfo.update();
	}
	
	public void tap(int i, int j)
	{
		currentInput.tap(i, j);
	}
	
	public void endTurn(boolean performEndTurn)
	{
		if (!InputBase.gameDraw.isActiveGame)
			return;
		currentInput.endTurn();
		if (performEndTurn)
			performEndTurn();
		beginTurn();
	}
	
	private void performEndTurn()
	{
		performEndTurn(new ActionGameEndTurn());
	}
	
	public void performEndTurn(ActionGameEndTurn action)
	{
		ActionResultGameEndTurn result = action.perform(game);
		InputBase.gameDraw.gameDrawUnitsHeal.start(result);
	}
	
}
