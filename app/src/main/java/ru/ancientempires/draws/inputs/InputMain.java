package ru.ancientempires.draws.inputs;

import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.actions.result.ActionResultGameEndTurn;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.draws.DrawUnitsHeal;

public class InputMain extends AbstractInput
{
	
	public static InputMain main;

	public InputPlayer         inputPlayer;
	public InputComputer       inputComputer;
	public AbstractPlayerInput currentInput;

	public InputMain(DrawMain drawMain)
	{
		InputMain.main = this;
		this.drawMain = drawMain;
		
		inputPlayer = new InputPlayer();
		inputComputer = new InputComputer();
		
		drawMain.info.update();
		drawMain.cells.update();
		drawMain.cellsDual.update();
		drawMain.units.update();
		drawMain.buildingSmokes.update();
	}
	
	public void beginTurn()
	{
		drawMain.isDrawCursor = false;
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
		drawMain.info.update();
	}
	
	public void tap(int i, int j)
	{
		currentInput.tap(i, j);
	}
	
	public void endTurn(boolean performEndTurn)
	{
		if (!drawMain.isActiveGame)
			return;
		if (currentInput != null) // первый игрок --- NONE
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
		drawMain.add(new DrawUnitsHeal().start(result));
	}
	
}
