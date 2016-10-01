package ru.ancientempires.draws.inputs;

import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.actions.result.ActionResultGameEndTurn;
import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.draws.DrawUnitsHeal;

public class InputMain extends AbstractInput {

	public InputPlayer         inputPlayer;
	public InputComputer       inputComputer;
	public AbstractPlayerInput currentInput;
	public GameActivity        activity;

	public InputMain(GameActivity activity, DrawMain drawMain) {
		super(null);
		this.activity = activity;
		this.drawMain = drawMain;
		game = activity.game;

		inputPlayer = new InputPlayer(this);
		inputComputer = new InputComputer(this);

		drawMain.inputMain = this;
		drawMain.inputPlayer = inputPlayer;

		drawMain.info.update();
		drawMain.cells.update();
		drawMain.cellsDual.update();
		drawMain.units.update();
		drawMain.buildingSmokes.update();
	}

	public void beginTurn() {
		if (currentInput != null)
			drawMain.focusOnCurrentPlayerCenter();
		drawMain.isDrawCursor = false;
		switch (game.currentPlayer.type) {
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

	public void tap(int i, int j) {
		currentInput.tap(i, j);
	}

	public void endTurn(boolean performEndTurn) {
		if (!drawMain.isActiveGame)
			return;
		if (currentInput != null) // первый игрок --- NONE
			currentInput.endTurn();
		if (performEndTurn)
			performEndTurn();
		beginTurn();
	}

	private void performEndTurn() {
		performEndTurn(new ActionGameEndTurn());
	}

	public void performEndTurn(ActionGameEndTurn action) {
		ActionResultGameEndTurn result = action.perform(game);
		drawMain.add(new DrawUnitsHeal(drawMain).start(result));
	}
}
