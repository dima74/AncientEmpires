package ru.ancientempires.view.inputs;

import ru.ancientempires.NoticeUnitBuy;
import ru.ancientempires.UnitBuyDialog;
import ru.ancientempires.action.ActionCellBuy;
import ru.ancientempires.action.ActionGetCellBuy;
import ru.ancientempires.action.ActionUnitCapture;
import ru.ancientempires.action.ActionUnitRepair;
import ru.ancientempires.action.result.ActionResultGetCellBuy;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.handler.ActionHelper;
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
		tapWithoutAction(game.currentPlayer.cursorI, game.currentPlayer.cursorJ);
		InputBase.gameDraw.focusOnCell(game.currentPlayer.cursorI, game.currentPlayer.cursorJ);
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
			if (new ActionHelper().canUnitRepair(i, j))
				tryRepair(i, j);
			else if (new ActionHelper().canUnitCapture(i, j))
				tryCapture(i, j);
			else if (new ActionHelper().canBuyOnCell(i, j))
				tryBuy(i, j);
		}
		else
		{
			boolean isMove = false;
			if (inputUnit.isActive)
				isMove = inputUnit.tap(i, j);
			if (!inputUnit.start(i, j) && !isMove && new ActionHelper().canBuyOnCell(i, j))
				tryBuy(i, j);
		}
	}
	
	public void tapWithoutAction(int i, int j)
	{
		lastTapI = i;
		lastTapJ = j;
		game.currentPlayer.cursorI = i;
		game.currentPlayer.cursorJ = j;
		InputBase.gameDraw.updateCursors();
	}
	
	private boolean tryRepair(int i, int j)
	{
		new ActionUnitRepair()
				.setIJ(i, j)
				.perform();
		InputBase.gameDraw.gameDrawCells.updateOneCell(i, j);
		return true;
	}
	
	private boolean tryCapture(int i, int j)
	{
		new ActionUnitCapture()
				.setIJ(i, j)
				.perform();
		InputBase.gameDraw.gameDrawCells.updateOneCell(i, j);
		return true;
	}
	
	private boolean tryBuy(int i, int j)
	{
		ActionResultGetCellBuy result = (ActionResultGetCellBuy) new ActionGetCellBuy()
				.setIJ(i, j)
				.perform();
		Unit[] units = result.units;
		boolean[] isAvailable = result.isAvailable;
		UnitBuyDialog.showDialog(this, units, isAvailable);
		return true;
	}
	
	@Override
	public void onUnitBuy(int iUnit)
	{
		new ActionCellBuy()
				.setUnit(iUnit)
				.setIJ(lastTapI, lastTapJ)
				.perform();
				
		InputBase.gameDraw.gameDrawInfo.update();
		inputUnit.start(lastTapI, lastTapJ);
		
		GameActivity.activity.view.thread.needUpdateCampaign = true;
	}
	
	@Override
	public void endTurn()
	{
		if (inputUnit.isActive)
			inputUnit.destroy();
	}
	
}
