package ru.ancientempires.draws;

import android.graphics.Canvas;

import ru.ancientempires.images.bitmaps.FewBitmaps;

public class DrawCells extends Draw {

	// private final int h = game.h;
	// private final int w = game.w;
	// private final int availableY;
	// private final int availableX;

	private boolean        isDual = false;
	public  FewBitmaps[][] field  = new FewBitmaps[game.h][game.w];
	public  boolean[][]    keep   = new boolean[game.h][game.w];

	public DrawCells(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public DrawCells setDual() {
		isDual = true;
		return this;
	}

	public void update() {
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
				updateCell(i, j);
	}

	public void updateCell(int i, int j) {
		if (!keep[i][j])
			field[i][j] = CellImages().getCellBitmap(game.fieldCells[i][j], isDual);
	}

	@Override
	public void draw(Canvas canvas) {
		for (int i = mainBase.iMin; i < mainBase.iMax; i++)
			for (int j = mainBase.jMin; j < mainBase.jMax; j++) {
				updateCell(i, j);
				FewBitmaps bitmap = field[i][j];
				if (bitmap == null)
					continue;
				final int y = A * i - (isDual ? A : 0);
				final int x = A * j;
				canvas.drawBitmap(bitmap.getBitmap(), x, y, null);
			}
	}

}
