package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.HashMap;

import ru.ancientempires.actions.Action;
import ru.ancientempires.actions.ActionCellBuy;
import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.actions.ActionUnitAttack;
import ru.ancientempires.actions.ActionUnitCapture;
import ru.ancientempires.actions.ActionUnitMove;
import ru.ancientempires.actions.ActionUnitRaise;
import ru.ancientempires.actions.ActionUnitRepair;
import ru.ancientempires.framework.FileLoader;

public class ActionImages extends AbstractImages {

	private HashMap<Class<? extends Action>, Bitmap> actionBitmaps;
	public  int                                      h;
	public  int                                      w;

	public Bitmap getActionBitmap(Action action) {
		return actionBitmaps.get(action.getClass());
	}

	@Override
	public void preload(FileLoader loader) throws IOException {
		actionBitmaps = new HashMap<>();
		String[] actionImageNames = new String[]
				{
						"action_cell_buy.png",
						"action_unit_move.png",
						"action_unit_capture.png",
						"action_unit_capture.png",
						"action_unit_attack.png",
						"action_unit_raise.png",
						"action_end_turn.png",
				};

		Action[] actionTypes = new Action[]
				{
						new ActionCellBuy(),
						new ActionUnitMove(),
						new ActionUnitRepair(),
						new ActionUnitCapture(),
						new ActionUnitAttack(),
						new ActionUnitRaise(),
						new ActionGameEndTurn()
				};

		for (int i = 0; i < actionImageNames.length; i++)
			actionBitmaps.put(actionTypes[i].getClass(), loader.loadImageAndResize(actionImageNames[i], 2.5f));
		Bitmap anyBitmap = actionBitmaps.entrySet().iterator().next().getValue();
		h = anyBitmap.getHeight();
		w = anyBitmap.getWidth();
	}
}
