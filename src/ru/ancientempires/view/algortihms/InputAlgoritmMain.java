package ru.ancientempires.view.algortihms;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public class InputAlgoritmMain
{
	
	public GameDrawMain				gameDraw;
	
	// Модель игры
	private final Game				game	= Client.getClient().getGame();
	
	public int						lastTapI;
	public int						lastTapJ;
	
	private InputAlgorithmUnitRange	inputAlgorithmUnitMove;
	private InputAlgorithmUnitRange	inputAlgorithmUnitAttack;
	private InputAlgorithmUnitRange	inputAlgorithmUnitRaise;
	
	// public Set<InputAlgorithm> inputAlgorithms = new LinkedHashSet<InputAlgorithm>();
	public InputAlgorithmUnitRange	currentInputAlgorithmUnitRange;
	
	public InputAlgoritmMain(GameDrawMain gameDraw)
	{
		this.gameDraw = gameDraw;
		for (GameDraw gameDrawPart : gameDraw.gameDraws)
			gameDrawPart.update(this.game);
		
		this.inputAlgorithmUnitMove = new InputAlgorithmUnitMove(this);
		this.inputAlgorithmUnitAttack = new InputAlgorithmUnitAttack(this);
		this.inputAlgorithmUnitRaise = new InputAlgorithmUnitRaise(this);
		
		tap(this.game.currentPlayer.cursorI, this.game.currentPlayer.cursorJ);
	}
	
	public boolean tap(int i, int j)
	{
		if (i == this.lastTapI && j == this.lastTapJ)
			return false;
		
		if (!this.game.map.validateCoord(i, j))
			return false;
		
		boolean isAction = this.currentInputAlgorithmUnitRange != null && this.currentInputAlgorithmUnitRange.tap(i, j);
		// for (InputAlgorithm inputAlgorithm : this.inputAlgorithms)
		// if (inputAlgorithm.tap(i, j))
		// {
		// isAction = true;
		// break;
		// }
		
		tapWithoutAction(i, j);
		this.gameDraw.gameDrawInfo.update(this.game);
		if (!isAction)
			this.gameDraw.gameDrawAction.update(this.lastTapI, this.lastTapJ);
		return false;
	}
	
	public void tapWithoutAction(int i, int j)
	{
		this.lastTapI = i;
		this.lastTapJ = j;
		this.game.currentPlayer.cursorI = i;
		this.game.currentPlayer.cursorJ = j;
		this.gameDraw.updateCursors(this.game);
	}
	
	public void performAction(ActionType actionType)
	{
		boolean isAction = false;
		
		InputAlgorithmUnitRange currentInputAlgorithm = null;
		if (actionType == ActionType.ACTION_UNIT_MOVE)
			currentInputAlgorithm = this.inputAlgorithmUnitMove;
		if (actionType == ActionType.ACTION_UNIT_ATTACK)
			currentInputAlgorithm = this.inputAlgorithmUnitAttack;
		if (actionType == ActionType.ACTION_UNIT_RAISE)
			currentInputAlgorithm = this.inputAlgorithmUnitRaise;
		
		if (currentInputAlgorithm != null)
		{
			// if (this.inputAlgorithms.contains(currentInputAlgorithm))
			if (this.currentInputAlgorithmUnitRange == currentInputAlgorithm)
				currentInputAlgorithm.end();
			// this.currentInputAlgorithmUnitRange = null;
			else
			{
				// for (InputAlgorithm inputAlgorithm : this.inputAlgorithms)
				// inputAlgorithm.destroy();
				if (this.currentInputAlgorithmUnitRange != null)
					this.currentInputAlgorithmUnitRange.revertState();
				
				isAction = true;
				// this.inputAlgorithms.add(currentInputAlgorithm);
				this.currentInputAlgorithmUnitRange = currentInputAlgorithm;
				currentInputAlgorithm.start(this.lastTapI, this.lastTapJ);
			}
		}
		else if (actionType == ActionType.ACTION_UNIT_REPAIR
			|| actionType == ActionType.ACTION_UNIT_CAPTURE)
		{
			Action action = new Action(actionType);
			action.setProperty("i", this.lastTapI);
			action.setProperty("j", this.lastTapJ);
			Client.action(action);
			
			this.gameDraw.gameDrawCell.update(this.game);
			this.gameDraw.gameDrawCellDual.update(this.game);
		}
		else if (actionType == ActionType.ACTION_CELL_BUY)
		{
			Action action = new Action(ActionType.GET_CELL_BUY_UNITS);
			action.setProperty("i", this.lastTapI);
			action.setProperty("j", this.lastTapJ);
			ActionResult result = Client.action(action);
			
			UnitType[] units = (UnitType[]) result.getProperty("units");
			this.gameDraw.gameActivity.startUnitBuyActivity(units);
		}
		else if (actionType == ActionType.ACTION_END_TURN)
		{
			// for (InputAlgorithm inputAlgorithm : this.inputAlgorithms)
			// inputAlgorithm.destroy();
			if (this.currentInputAlgorithmUnitRange != null)
				this.currentInputAlgorithmUnitRange.revertState();
			// this.currentInputAlgorithmUnitRange = null;
			
			Action action = new Action(actionType);
			Client.action(action);
			
			this.gameDraw.gameDrawUnit.update(this.game);
			this.gameDraw.gameDrawInfo.update(this.game);
			tap(this.game.currentPlayer.cursorI, this.game.currentPlayer.cursorJ);
			// Toast.makeText(getContext(), "Новый Ход!", Toast.LENGTH_SHORT).show();
		}
		else
			MyAssert.a(false);
		if (!isAction)
			this.gameDraw.gameDrawAction.update(this.lastTapI, this.lastTapJ);
	}
	
	public void performActionBuy(UnitType type)
	{
		Action action = new Action(ActionType.ACTION_CELL_BUY);
		action.setProperty("i", this.lastTapI);
		action.setProperty("j", this.lastTapJ);
		action.setProperty("type", type.ordinal);
		Client.action(action);
		
		this.gameDraw.gameDrawUnit.update(this.game);
		this.gameDraw.gameDrawInfo.update(this.game);
		this.gameDraw.gameDrawAction.update(this.lastTapI, this.lastTapJ);
	}
	
}
