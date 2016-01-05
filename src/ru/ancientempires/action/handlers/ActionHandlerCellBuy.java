package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Unit;

public class ActionHandlerCellBuy extends ActionHandler
{
	
	public ActionHandlerCellBuy(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerCellBuy(this.type);
	}
	
	private int		i;
	private int		j;
	private int		iUnit;
	private Unit	unit;
	
	@Override
	public ActionResult action(Action action)
	{
		this.i = (int) action.getProperty("i");
		this.j = (int) action.getProperty("j");
		this.iUnit = (int) action.getProperty("unit");
		
		this.result.successfully = checkBuy() && buy();
		return this.result;
	}
	
	private boolean checkBuy()
	{
		if (!(GameHandler.checkCoord(this.i, this.j) && checkPlayer()))
			return false;
		Action actionGet = new Action(ActionType.GET_BUY);
		actionGet.setProperty("i", this.i);
		actionGet.setProperty("j", this.j);
		ActionResult result = Client.action(actionGet);
		this.unit = ((Unit[]) result.getProperty("units"))[this.iUnit];
		if (!this.unit.type.isStatic)
			this.unit = new Unit(this.unit.type, this.unit.player);
		return ActionHandlerHelper.isEmptyCells(this.i, this.j, this.unit.type);
	}
	
	private boolean checkPlayer()
	{
		return GameHandler.fieldCells[this.i][this.j].player == GameHandler.game.currentPlayer;
	}
	
	private boolean buy()
	{
		this.unit.health = this.unit.type.baseHealth;
		this.unit.player = GameHandler.game.currentPlayer;
		this.unit.player.units.add(this.unit);
		
		GameHandler.game.currentPlayer.gold -= this.unit.cost;
		GameHandler.setUnit(this.i, this.j, this.unit);
		
		if (this.unit.type.isStatic)
		{
			MyAssert.a(GameHandler.game.staticUnitsDead[this.unit.player.ordinal].contains(this.unit));
			GameHandler.game.staticUnitsDead[this.unit.player.ordinal].remove(this.unit);
			this.unit.cost += 200;
		}
		
		return true;
	}
	
}
