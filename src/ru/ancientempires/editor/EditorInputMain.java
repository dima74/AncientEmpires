package ru.ancientempires.editor;

import java.util.ArrayList;

import ru.ancientempires.activity.EditorActivity;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public class EditorInputMain
{
	
	public Game				game	= EditorActivity.activity.game;
	public EditorDrawMain	drawMain;
	public Unit				unit;
	public Cell[]			cells;
	public Cell[][]			groups	= new Cell[2][];
									
	public EditorInputMain(EditorDrawMain drawMain)
	{
		this.drawMain = drawMain;
		createCellGroups();
		update();
	}
	
	private void createCellGroups()
	{
		Rules rules = game.rules;
		// for (UnitType type : rules.unitTypes)
		// if (!type.isHidden)
		// unit = new Unit(type, game.players[0], game);
		unit = new Unit(rules.unitTypes[2], game.players[0], game);
		
		ArrayList<Cell>[] groupsList = new ArrayList[2];
		groupsList[0] = new ArrayList<Cell>();
		groupsList[1] = new ArrayList<Cell>();
		for (CellType type : rules.cellTypes)
			groupsList[type.isHealing ? 0 : 1].add(new Cell(type));
		groups[0] = groupsList[0].toArray(new Cell[0]);
		groups[1] = groupsList[1].toArray(new Cell[0]);
		
		cells = new Cell[2];
		cells[0] = groups[0][0];
		cells[1] = groups[1][1];
	}
	
	private void update()
	{
		drawMain.choose.setBitmap(0, UnitImages.get().getUnitBitmap(unit, false).getBitmap());
		drawMain.choose.setBitmap(1, CellImages.get().getCellBitmap(cells[0], false));
		drawMain.choose.setBitmap(2, CellImages.get().getCellBitmap(cells[1], false));
	}
	
	public void tap(int i, int j)
	{
		int selected = drawMain.choose.selectedBitmap;
		if (selected == 0)
		{
			game.setUnit(i, j, new Unit(unit));
			drawMain.units.update();
		}
		else
		{
			game.fieldCells[i][j] = new Cell(cells[selected - 1]);
			drawMain.cells.update();
		}
	}
	
	public void choose(int i)
	{
	
	}
	
}
