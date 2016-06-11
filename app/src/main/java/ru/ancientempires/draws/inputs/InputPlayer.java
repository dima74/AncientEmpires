package ru.ancientempires.draws.inputs;

import ru.ancientempires.NoticeUnitBuy;
import ru.ancientempires.UnitBuyDialog;
import ru.ancientempires.action.ActionCellBuy;
import ru.ancientempires.action.ActionGetCellBuy;
import ru.ancientempires.action.ActionUnitCapture;
import ru.ancientempires.action.ActionUnitRepair;
import ru.ancientempires.action.result.ActionResultGetCellBuy;
import ru.ancientempires.handler.ActionHelper;

public class InputPlayer extends AbstractPlayerInput implements NoticeUnitBuy
{
	
	public int lastTapI;
	public int lastTapJ;

	public InputUnit inputUnit = new InputUnit(this);

	@Override
	public void beginTurn()
	{
		tapWithoutAction(game.currentPlayer.cursorI, game.currentPlayer.cursorJ);
		drawMain.focusOnCell(game.currentPlayer.cursorI, game.currentPlayer.cursorJ);
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
		tapWithoutAction(i, j);
		drawMain.isDrawCursor = true;
		
		if (!inputUnit.isActive)
		{
			boolean start = inputUnit.start(i, j);
			if (!start)
				tryCellActions(i, j);
		}
		else
		{
			boolean isUnitAction = inputUnit.tap(i, j); // сам станет неактивным
			if (isUnitAction)
				inputUnit.start(i, j);
			else
			{
				boolean isSameTap = inputUnit.isSameTap(i, j);
				if (isSameTap)
					tryCellActions(i, j);
				else
				{
					boolean start = inputUnit.start(i, j);
					if (!start)
						tryCellActions(i, j);
				}
			}
		}
	}
	
	private void tryCellActions(int i, int j)
	{
		if (new ActionHelper(game).canUnitRepair(i, j))
			tryRepair(i, j);
		else if (new ActionHelper(game).canUnitCapture(i, j))
			tryCapture(i, j);
		else if (new ActionHelper(game).canBuyOnCell(i, j))
			tryBuy(i, j);
	}
	
	public void tapWithoutAction(int i, int j)
	{
		lastTapI = i;
		lastTapJ = j;
		game.currentPlayer.cursorI = i;
		game.currentPlayer.cursorJ = j;
		drawMain.updateCursors();
	}
	
	private boolean tryRepair(int i, int j)
	{
		new ActionUnitRepair()
				.setIJ(i, j)
				.perform(game);
		drawMain.postUpdateCampaign();
		return true;
	}
	
	private boolean tryCapture(int i, int j)
	{
		new ActionUnitCapture()
				.setIJ(i, j)
				.perform(game);
		drawMain.postUpdateCampaign();
		return true;
	}
	
	private boolean tryBuy(int i, int j)
	{
		ActionResultGetCellBuy result = (ActionResultGetCellBuy) new ActionGetCellBuy()
				.setIJ(i, j)
				.perform(game);
		new UnitBuyDialog().showDialog(this, result);
		return true;
	}
	
	@Override
	public void onUnitBuy(int iUnit)
	{
		new ActionCellBuy()
				.setUnit(iUnit)
				.setIJ(lastTapI, lastTapJ)
				.perform(game);

		drawMain.info.update();
		inputUnit.start(lastTapI, lastTapJ);
		drawMain.postUpdateCampaign();
	}
	
	@Override
	public void endTurn()
	{
		if (inputUnit.isActive)
			inputUnit.destroy();
	}
	
}
