package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.action.result.ActionResultGetCellBuy;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class ActionCellBuy extends ActionFrom
{
	
	private int iUnit;
	
	public ActionCellBuy setUnit(int iUnit)
	{
		this.iUnit = iUnit;
		return this;
	}
	
	@Override
	public ActionResult perform(Game game)
	{
		performBase(game);
		return null;
	}
	
	@Override
	public boolean check()
	{
		if (!(super.check() && checkPlayer()))
			return false;
		Unit unit = getUnit();
		return new ActionHelper(game).isEmptyCells(i, j, unit);
	}
	
	private boolean checkPlayer()
	{
		Player player = game.fieldCells[i][j].player;
		return player == game.currentPlayer && player.numberUnits() < game.unitsLimit;
	}
	
	private Unit getUnit()
	{
		ActionGetCellBuy actionGet = new ActionGetCellBuy();
		actionGet.setIJ(i, j);
		ActionResultGetCellBuy result = actionGet.perform(game);
		Unit unit = result.units[iUnit];
		if (!unit.type.isStatic)
			unit = new Unit(game, unit.type, unit.player);
		return unit;
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = getUnit();
		unit.health = unit.type.healthDefault;
		unit.player = game.currentPlayer;
		unit.player.units.add(unit);
		
		game.currentPlayer.gold -= unit.getCost();
		game.setUnit(i, j, unit);
		
		if (unit.type.isStatic)
		{
			MyAssert.a(game.unitsStaticDead[unit.player.ordinal].contains(unit));
			game.unitsStaticDead[unit.player.ordinal].remove(unit);
			unit.numberBuys++;
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
