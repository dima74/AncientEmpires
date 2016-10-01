package ru.ancientempires.editor;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ru.ancientempires.MyColor;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class EditorStructUnit extends EditorStruct {

	public Unit unit;

	public EditorStructUnit(Game game, Unit unit) {
		super(game);
		this.unit = unit;
	}

	@Override
	public void setColor(MyColor color) {
		UnitType type = unit.type.templateType == null ? unit.type : unit.type.templateType;
		unit = new Unit(game, type, getPlayer(color));
	}

	@Override
	public void drawBitmap(Canvas canvas) {
		Bitmap bitmap = UnitImages.get().getUnitBitmap(unit, false).getBitmap();
		drawBitmap(canvas, bitmap);
	}

	@Override
	public EditorStruct createCopy() {
		return new EditorStructUnit(game, new Unit(unit)).setYX(y, x);
	}
}
