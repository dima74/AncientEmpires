package ru.ancientempires.view.inputs;

import ru.ancientempires.NoticeUnitBuy;
import ru.ancientempires.UnitBuyDialog;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.action.handlers.ActionHandlerHelper;
import ru.ancientempires.client.Client;
import ru.ancientempires.model.Unit;

public class InputPlayer extends InputBase implements NoticeUnitBuy
{
	
	public int	lastTapI;
	public int	lastTapJ;
	
	public InputUnit inputUnit;
	
	public InputPlayer()
	{
		inputUnit = new InputUnit(this);
	}
	
	@Override
	public void beginTurn()
	{
		tapWithoutAction(InputBase.game.currentPlayer.cursorI, InputBase.game.currentPlayer.cursorJ);
		InputBase.gameDraw.focusOnCell(InputBase.game.currentPlayer.cursorI, InputBase.game.currentPlayer.cursorJ);
	}
	
	/*
	 Тап:
	 	Поле не активно:
	 		Получить поле хода+атаки+воскрешения. Отобразить вместе.
	 		Если оно пусто и на этой клеточке можно что-нибудь купить, то вызвать диалог.
	 	Поле активно:
	 		тап на ту же клетку:
	 			захватываем/чиним если можем
	 			если можем что-нибудь купить, то вызываем диалог. 
	 			(завершить ход виспом стоя на месте в крепости можно будет из контекстного меню) 
	 		на другую: атакуем/ходим/воскрешаем если можем и то и другое, то вызываем диалог
	 Конец хода будет в контекстном меню.
	 */
	@Override
	public void tap(int i, int j)
	{
		boolean sameTap = i == lastTapI && j == lastTapJ && InputBase.gameDraw.isDrawCursor;
		tapWithoutAction(i, j);
		InputBase.gameDraw.isDrawCursor = true;
		if (sameTap)
		{
			if (inputUnit.isActive)
				inputUnit.tap(i, j); // сам станет неактивным
			if (ActionHandlerHelper.canBuyOnCell(i, j))
				tryBuy(i, j);
			else if (ActionHandlerHelper.canUnitRepair(i, j))
				tryRepair(i, j);
			else if (ActionHandlerHelper.canUnitCapture(i, j))
				tryCapture(i, j);
		}
		else
		{
			boolean isMove = false;
			if (inputUnit.isActive)
				isMove = inputUnit.tap(i, j);
			if (!inputUnit.start(i, j) && !isMove && ActionHandlerHelper.canBuyOnCell(i, j))
				tryBuy(i, j);
		}
	}
	
	public void tapWithoutAction(int i, int j)
	{
		lastTapI = i;
		lastTapJ = j;
		InputBase.game.currentPlayer.cursorI = i;
		InputBase.game.currentPlayer.cursorJ = j;
		InputBase.gameDraw.updateCursors();
	}
	
	private boolean tryBuy(int i, int j)
	{
		Action action = new Action(ActionType.GET_BUY);
		action.setProperty("i", i);
		action.setProperty("j", j);
		ActionResult result = Client.action(action);
		
		Unit[] units = (Unit[]) result.getProperty("units");
		boolean[] isAvailable = (boolean[]) result.getProperty("isAvailable");
		InputBase.thread.isPause = true;
		
		UnitBuyDialog.showDialog(this, units, isAvailable);
		return true;
	}
	
	private boolean tryRepair(int i, int j)
	{
		Action action = new Action(ActionType.ACTION_UNIT_REPAIR);
		action.setProperty("i", i);
		action.setProperty("j", j);
		Client.action(action);
		InputBase.gameDraw.gameDrawCells.updateOneCell(i, j);
		return true;
	}
	
	private boolean tryCapture(int i, int j)
	{
		Action action = new Action(ActionType.ACTION_UNIT_CAPTURE);
		action.setProperty("i", i);
		action.setProperty("j", j);
		Client.action(action);
		InputBase.gameDraw.gameDrawCells.updateOneCell(i, j);
		return true;
	}
	
	@Override
	public void onUnitBuy(int iUnit)
	{
		Action action = new Action(ActionType.ACTION_CELL_BUY);
		action.setProperty("i", lastTapI);
		action.setProperty("j", lastTapJ);
		action.setProperty("unit", iUnit);
		Client.action(action);
		
		InputBase.gameDraw.gameDrawInfo.update();
		inputUnit.start(lastTapI, lastTapJ);
		
		InputBase.thread.needUpdateCampaign = true;
		InputBase.thread.isPause = false;
	}
	
	@Override
	public void endTurn()
	{
		if (inputUnit.isActive)
			inputUnit.destroy();
	}
	
}
