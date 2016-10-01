package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.model.Cell;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionActivateStruct extends ActionFrom {

	@Override
	public void performQuick() {
		Cell cell = game.fieldCells[i][j];
		cell.type.struct.activate(cell);
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
	}

	public ActionActivateStruct fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		return this;
	}
}
