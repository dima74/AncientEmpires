package ru.ancientempires.view.inputs;

import ru.ancientempires.action.ActionGameEndTurn;
import ru.ancientempires.action.result.ActionResultGameEndTurn;
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
		switch (game.currentPlayer.type)
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
		
		ActionResultGameEndTurn result = new ActionGameEndTurn().perform();
		InputBase.gameDraw.gameDrawUnitsHeal.start(result);
		
		beginTurn();
	}
	
}
