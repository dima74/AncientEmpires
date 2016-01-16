package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.action.result.ActionResultGetCellBuy;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.ActionHelper;
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
	public ActionResult perform()
	{
		if (!check(checkBuy()))
			return null;
		performQuick();
		return commit();
	}
	
	private boolean checkBuy()
	{
		if (!(game.checkCoordinates(i, j) && checkPlayer()))
			return false;
		ActionResultGetCellBuy result = (ActionResultGetCellBuy) new ActionGetCellBuy()
				.setIJ(i, j)
				.perform();
		unit = result.units[iUnit];
		if (!unit.type.isStatic)
			unit = new Unit(unit.type, unit.player);
		return new ActionHelper().isEmptyCells(i, j, unit.type);
	}
	
	private boolean checkPlayer()
	{
		return game.fieldCells[i][j].player == game.currentPlayer;
	}
	
	@Override
	public void performQuick()
	{
		unit.health = unit.type.baseHealth;
		unit.player = game.currentPlayer;
		unit.player.units.add(unit);
		
		game.currentPlayer.gold -= unit.cost;
		game.setUnit(i, j, unit);
		
		if (unit.type.isStatic)
		{
			MyAssert.a(game.unitsStaticDead[unit.player.ordinal].contains(unit));
			game.unitsStaticDead[unit.player.ordinal].remove(unit);
			unit.cost += 200;
		}
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
