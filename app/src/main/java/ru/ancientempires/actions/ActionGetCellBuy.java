package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.ancientempires.actions.result.ActionResultGetCellBuy;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionGetCellBuy extends ActionFrom {

	private ActionResultGetCellBuy result;

	@Override
	public boolean changesGame() {
		return false;
	}

	@Override
	public ActionResultGetCellBuy perform(Game game) {
		performBase(game);
		return result;
	}

	@Override
	public void performQuick() {
		Cell cell = game.fieldCells[i][j];
		CellType cellType = cell.type;

		List<Unit> cellUnits = Arrays.asList(game.buyUnits[cell.player.ordinal][cellType.ordinal]);
		ArrayList<Unit> units = new ArrayList<>(game.allowedUnits == -1 ? cellUnits : cellUnits.subList(0, game.allowedUnits));
		units.addAll(0, game.unitsStaticDead[cell.player.ordinal]);
		result = new ActionResultGetCellBuy(units);

		BuyStatus[] status = result.statuses;
		for (int k = 0; k < status.length; k++)
			if (cell.player.numberUnits() >= cell.player.unitsLimit())
				status[k] = BuyStatus.UNIT_LIMIT_REACHED;
			else if (game.currentPlayer.gold < units.get(k).getCost())
				status[k] = BuyStatus.NO_GOLD;
			else if (!new ActionHelper(game).isEmptyCells(i, j, units.get(k)))
				status[k] = BuyStatus.NO_PLACE;
			else
				status[k] = BuyStatus.SUCCESS;
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
	}

	public ActionGetCellBuy fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		return this;
	}

}
