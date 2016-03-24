package ru.ancientempires.editor;

import java.util.ArrayList;

import ru.ancientempires.MyColor;
import ru.ancientempires.activity.EditorActivity;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.rules.Rules;

public class EditorInputMain implements Callback
{
	
	public Game				game			= EditorActivity.activity.game;
	public EditorDrawMain	drawMain;
							
	public EditorStruct[]	structsCurrent	= new EditorStruct[3];
	public EditorStruct[][]	structs			= new EditorStruct[3][];
											
	public EditorInputMain(EditorDrawMain drawMain)
	{
		this.drawMain = drawMain;
		createStructs();
		update();
	}
	
	private void createStructs()
	{
		Rules rules = game.rules;
		ArrayList<EditorStruct>[] structsList = new ArrayList[3];
		for (int i = 0; i < 3; i++)
			structsList[i] = new ArrayList<>();
			
		// units
		for (UnitType type : rules.unitTypes)
			if (!"DEFAULT".equals(type.name) && !"KING".equals(type.name))
				structsList[0].add(new EditorStructUnit(game, new Unit(type, game.players[0], game)));
				
		// cells
		for (CellType type : rules.cellTypes)
			if (!type.name.contains("DEFAULT") && !type.name.contains("GROUP"))
				structsList[type.isHealing ? 1 : 2].add(new EditorStructCell(game, new Cell(type)));
				
		for (int i = 0; i < 3; i++)
			structs[i] = structsList[i].toArray(new EditorStruct[0]);
	}
	
	public void update()
	{
		for (int i = 0; i < 3; i++)
			structsCurrent[i] = structs[i][0];
		drawMain.choose.create(structsCurrent, this);
	}
	
	@Override
	public void tapChoose(int i)
	{
		if (i == drawMain.choose.selectedBitmap)
		{
			MyColor[] colors = null;
			if (i == 0)
				colors = MyColor.playersColors();
			if (i == 1)
				colors = MyColor.values();
			if (i == 2)
				colors = new MyColor[0];
			new EditorChooseDialog().show(structs[i], colors);
		}
	}
	
	public void tap(int i, int j)
	{
		int selected = drawMain.choose.selectedBitmap;
		EditorStruct struct = structsCurrent[selected];
		if (selected == 0)
		{
			game.setUnit(i, j, new Unit(((EditorStructUnit) struct).unit));
			drawMain.units.update();
		}
		else
		{
			game.fieldCells[i][j] = new Cell(((EditorStructCell) struct).cell);
			drawMain.cells.update();
		}
	}
	
}
