package ru.ancientempires.editor;

import java.util.ArrayList;

import ru.ancientempires.MyColor;
import ru.ancientempires.activity.EditorActivity;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.UnitImages;
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
							
	public int[]			colorsSelected	= new int[3];
	public EditorStruct[][]	structs			= new EditorStruct[3][];
											
	public EditorInputMain(EditorDrawMain drawMain)
	{
		this.drawMain = drawMain;
		createStructs();
	}
	
	private void createStructs()
	{
		Rules rules = game.rules;
		ArrayList<EditorStruct>[] structsList = new ArrayList[3];
		for (int i = 0; i < 3; i++)
			structsList[i] = new ArrayList<>();
			
		for (UnitType type : rules.unitTypes)
			if (UnitImages.get().containsBitmap(type) && type.templateType == null)
				structsList[0].add(new EditorStructUnit(game, new Unit(game, type, game.players[0])));
		for (CellType type : rules.cellTypes)
			if (CellImages.get().containsBitmap(type))
				structsList[type.isHealing ? 1 : 2].add(new EditorStructCell(game, new Cell(game, type)));
				
		for (int i = 0; i < 3; i++)
			structs[i] = structsList[i].toArray(new EditorStruct[0]);
		drawMain.choose.create(3, this);
		for (int i = 0; i < 3; i++)
			drawMain.choose.setStruct(i, structs[i][0]);
	}
	
	@Override
	public void tapChoose(int i)
	{
		int selected = drawMain.choose.selected;
		if (i == selected)
		{
			MyColor[] colors = null;
			if (i == 0)
				colors = MyColor.playersColors();
			if (i == 1)
				colors = MyColor.values();
			if (i == 2)
				colors = new MyColor[0];
			new EditorChooseDialog().show(structs[i], colors, colorsSelected[selected]);
		}
	}
	
	public void tapMap(int i, int j)
	{
		int selected = drawMain.choose.selected;
		EditorStruct struct = drawMain.choose.structs[selected];
		if (selected == 0)
		{
			if (game.fieldUnits[i][j] != null)
				game.fieldUnits[i][j] = null;
			else
				game.setUnit(i, j, new Unit(((EditorStructUnit) struct).unit));
		}
		else
			game.fieldCells[i][j] = new Cell(((EditorStructCell) struct).cell);
	}
	
	public void setStruct(int i, int color)
	{
		int selected = drawMain.choose.selected;
		drawMain.choose.setStruct(selected, structs[selected][i]);
		colorsSelected[selected] = color;
	}
	
}
