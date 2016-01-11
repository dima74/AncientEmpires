package ru.ancientempires.action.handlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Unit;

public class ActionCellBuy extends ActionFrom
{
	
	private int		iUnit;
	private Unit	unit;
	
	public ActionCellBuy setUnit(int iUnit)
	{
		this.iUnit = iUnit;
		return this;
	}
	
	@Override
	public ActionResult action()
	{
		result.successfully = checkBuy() && buy();
		return result;
	}
	
	private boolean checkBuy()
	{
		if (!(GameHandler.checkCoord(i, j) && checkPlayer()))
			return false;
		Action actionGet = new ActionGetCellBuy().setIJ(i, j);
		ActionResult result = Client.action(actionGet);
		unit = ((Unit[]) result.getProperty("units"))[iUnit];
		if (!unit.type.isStatic)
			unit = new Unit(unit.type, unit.player);
		return ActionHandlerHelper.isEmptyCells(i, j, unit.type);
	}
	
	private boolean checkPlayer()
	{
		return GameHandler.fieldCells[i][j].player == GameHandler.game.currentPlayer;
	}
	
	private boolean buy()
	{
		unit.health = unit.type.baseHealth;
		unit.player = GameHandler.game.currentPlayer;
		unit.player.units.add(unit);
		
		GameHandler.game.currentPlayer.gold -= unit.cost;
		GameHandler.setUnit(i, j, unit);
		
		if (unit.type.isStatic)
		{
			MyAssert.a(GameHandler.game.unitsStaticDead[unit.player.ordinal].contains(unit));
			GameHandler.game.unitsStaticDead[unit.player.ordinal].remove(unit);
			unit.cost += 200;
		}
		
		return true;
	}
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		super.load(input);
		iUnit = input.readShort();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		super.save(output);
		output.writeShort(iUnit);
	}
	
}
