package ru.ancientempires.editor;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ru.ancientempires.MyColor;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;

public class EditorStructCell extends EditorStruct {

	public Cell cell;

	public EditorStructCell(Game game, Cell cell) {
		super(game);
		this.cell = cell;
	}

	@Override
	public void setColor(MyColor color) {
		cell.player = cell.type.isCapturing ? getPlayer(color) : null;
	}

	@Override
	public void drawBitmap(Canvas canvas) {
		FewBitmaps fewBitmap = CellImages.get().getCellBitmap(cell, false);
		drawBitmap(canvas, fewBitmap.getBitmap());

		FewBitmaps fewBitmapDual = CellImages.get().getCellBitmap(cell, true);
		if (fewBitmapDual != null) {
			Bitmap bitmapDual = fewBitmapDual.getBitmap();
			drawBitmap(canvas, bitmapDual, x, y - bitmapDual.getHeight());
		}
	}

	@Override
	public EditorStruct createCopy() {
		return new EditorStructCell(game, new Cell(cell)).setYX(y, x);
	}

}
