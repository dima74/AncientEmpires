package ru.ancientempires.action.handlers;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Player;

public class PlayerHelper
{
	
	public static int getCastleNumber(Player player)
	{
		CellType castle = CellType.getType("CASTLE");
		int number = 0;
		for (Cell[] line : GameHandler.fieldCells)
			for (Cell cell : line)
				if (cell.type == castle && cell.player == player)
					number++;
		return number;
	}
	
}
