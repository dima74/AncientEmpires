package ru.ancientempires.action;

import java.util.ArrayList;

import ru.ancientempires.action.result.ActionResultGameEndTurn;
import ru.ancientempires.client.Client;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class ActionGameEndTurn extends Action
{
	
	public ActionResultGameEndTurn result = new ActionResultGameEndTurn();
	
	@Override
	public ActionResultGameEndTurn perform()
	{
		performQuick();
		return commit(result);
	}
	
	@Override
	public void performQuick()
	{
		Player oldPlayer = game.currentPlayer;
		Player newPlayer = game.players[(oldPlayer.ordinal + 1) % game.players.length];
		game.currentPlayer = newPlayer;
		
		clearPlayerState(oldPlayer);
		game.currentTurn++;
		Client.getGame().runTasks();
		healUnits();
		earnGold();
	}
	
	private void clearPlayerState(Player player)
	{
		for (Unit unit : player.units)
			new ActionHelper().clearUnitState(unit);
	}
	
	private void healUnits()
	{
		ArrayList<Unit> unitsToHeal = new ArrayList<Unit>();
		ArrayList<Integer> valueToHeal = new ArrayList<Integer>();
		
		for (Unit unit : game.currentPlayer.units)
		{
			Cell cell = game.fieldCells[unit.i][unit.j];
			if (cell.type.isHeal && (cell.player == unit.player || !cell.type.isCapture))
			{
				int oldHealth = unit.health;
				unit.health = Math.min(unit.type.baseHealth, unit.health + 20);
				int addHealth = unit.health - oldHealth;
				if (addHealth > 0)
				{
					unitsToHeal.add(unit);
					valueToHeal.add(addHealth);
				}
			}
		}
		
		result.unitsToHeal = unitsToHeal;
		result.valueToHeal = valueToHeal;
	}
	
	private void earnGold()
	{
		Player currentPlayer = game.currentPlayer;
		currentPlayer.gold += game.currentEarns[currentPlayer.ordinal];
	}
	
}
