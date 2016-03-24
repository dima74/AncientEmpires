package ru.ancientempires.editor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.MyColor;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;

public class EditorStructCell extends EditorStruct
{
	
	public Cell cell;
	
	public EditorStructCell(Game game, Cell cell)
	{
		super(game);
		this.cell = cell;
	}
	
	@Override
	public void setColor(MyColor color)
	{
		cell.player = cell.type.isCapturing ? getPlayer(color) : null;
	}
	
	@Override
	public void drawBitmap(Canvas canvas)
	{
		Bitmap bitmap = CellImages.get().getCellBitmap(cell, false);
		drawBitmap(canvas, bitmap);
		
		Bitmap bitmapDual = CellImages.get().getCellBitmap(cell, true);
		if (bitmapDual != null)
			drawBitmap(canvas, bitmapDual, x, y - bitmapDual.getHeight());
	}
	
}
