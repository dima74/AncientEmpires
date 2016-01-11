package ru.ancientempires.action.handlers;

import java.util.ArrayList;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.client.Client;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class ActionGameEndTurn extends Action
{
	
	@Override
	public ActionResult action()
	{
		Player oldPlayer = GameHandler.game.currentPlayer;
		Player newPlayer = GameHandler.game.players[(oldPlayer.ordinal + 1) % GameHandler.game.players.length];
		GameHandler.game.currentPlayer = newPlayer;
		
		clearPlayerState(oldPlayer);
		GameHandler.game.currentTurn++;
		Client.getGame().runTasks();
		healUnits();
		earnGold();
		
		return result;
	}
	
	private void clearPlayerState(Player player)
	{
		for (Unit unit : player.units)
			ActionHandlerHelper.clearUnitState(unit);
	}
	
	private void healUnits()
	{
		ArrayList<Unit> unitsToHeal = new ArrayList<Unit>();
		ArrayList<Integer> valueToHeal = new ArrayList<Integer>();
		
		for (Unit unit : GameHandler.game.currentPlayer.units)
		{
			Cell cell = GameHandler.fieldCells[unit.i][unit.j];
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
		
		result.setProperty("unitsToHeal", unitsToHeal);
		result.setProperty("valueToHeal", valueToHeal);
	}
	
	private void earnGold()
	{
		Player currentPlayer = GameHandler.game.currentPlayer;
		currentPlayer.gold += GameHandler.game.currentEarns[currentPlayer.ordinal];
	}
	
}
