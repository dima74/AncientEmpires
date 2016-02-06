package ru.ancientempires.action;

import java.util.ArrayList;

import ru.ancientempires.action.result.ActionResultGameEndTurn;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;

public class ActionGameEndTurn extends Action
{
	
	public ActionResultGameEndTurn result = new ActionResultGameEndTurn();
	
	@Override
	public ActionResultGameEndTurn perform(Game game)
	{
		performBase(game);
		return result;
	}
	
	@Override
	public void performQuick()
	{
		// System.out.printf("%d/%d %d\n", game.players[0].units.size(), game.h * game.w, System.currentTimeMillis());
		// if (game.currentTurn == 9)
		// System.out.println(game.isMain + " " + game.tasks.size());
		
		Player oldPlayer = game.currentPlayer;
		Player newPlayer = game.players[(oldPlayer.ordinal + 1) % game.players.length];
		game.currentPlayer = newPlayer;
		
		clearPlayerState(oldPlayer);
		game.currentTurn++;
		game.runTasks();
		healUnits();
		earnGold();
	}
	
	private void clearPlayerState(Player player)
	{
		for (Unit unit : player.units)
			new ActionHelper(game).clearUnitState(unit);
	}
	
	private void healUnits()
	{
		ArrayList<Unit> unitsToHeal = new ArrayList<Unit>();
		ArrayList<Integer> valueToHeal = new ArrayList<Integer>();
		
		for (Unit unit : game.currentPlayer.units)
		{
			Cell cell = game.fieldCells[unit.i][unit.j];
			if (cell.type.isHealing && (cell.player == unit.player || !cell.type.isCapturing))
			{
				int oldHealth = unit.health;
				unit.health = Math.min(unit.type.healthDefault, unit.health + 20);
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
