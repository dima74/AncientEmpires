package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.images.bitmaps.FewBitmaps;

public class CursorImages extends AbstractImages {

	public Bitmap cursorWay;
	public Bitmap cursorAttack;
	public Bitmap cursorMixed;

	public FewBitmaps cursor;
	public FewBitmaps cursorPointerAttack;
	public FewBitmaps cursorPointerWay;

	public int cursorH;
	public int cursorW;

	@Override
	public void preload(FileLoader loader) throws IOException {
		cursorWay = loader.loadImage("cursor_way.png");
		cursorAttack = loader.loadImage("cursor_attack.png");
		cursorMixed = loader.loadImage("cursor_mixed.png");
		cursor = new FewBitmaps(loader,
				"cursor_down.png",
				"cursor_up.png");
		cursorPointerAttack = new FewBitmaps(loader,
				"cursor_pointer_attack_0.png",
				"cursor_pointer_attack_1.png",
				"cursor_pointer_attack_2.png");
		cursorPointerWay = new FewBitmaps(loader, "cursor_pointer_way.png");

		cursorH = cursor.getBitmap().getHeight();
		cursorW = cursor.getBitmap().getWidth();
	}

}
