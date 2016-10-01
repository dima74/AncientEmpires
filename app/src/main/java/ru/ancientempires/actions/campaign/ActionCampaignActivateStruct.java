package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.ActionActivateStruct;
import ru.ancientempires.model.Cell;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignActivateStruct extends ActionActivateStruct {

	@Override
	public void performQuick() {
		Cell cell = game.fieldCells[i][j];
		cell.type.struct.activate(cell);
	}

	@Override
	public boolean check() {
		Cell cell = game.fieldCells[i][j];
		return cell.type.struct.canActivate(cell);
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
	}

	public ActionCampaignActivateStruct fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		return this;
	}
}
