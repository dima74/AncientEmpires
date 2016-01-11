package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.handlers.ActionCellBuy;
import ru.ancientempires.action.handlers.ActionGameEndTurn;
import ru.ancientempires.action.handlers.ActionUnitAttack;
import ru.ancientempires.action.handlers.ActionUnitCapture;
import ru.ancientempires.action.handlers.ActionUnitMove;
import ru.ancientempires.action.handlers.ActionUnitRaise;
import ru.ancientempires.action.handlers.ActionUnitRepair;

public class ActionImages extends IImages
{
	
	private Bitmap[]	actionBitmaps;
	public int			h;
	public int			w;
	
	public Bitmap getActionBitmap(Action action)
	{
		return actionBitmaps[action.ordinal()];
	}
	
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		actionBitmaps = new Bitmap[20];
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
			actionBitmaps[actionTypes[i].ordinal()] = loader.loadImageAndResize(actionImageNames[i], 2.5f);
		h = actionBitmaps[actionTypes[0].ordinal()].getHeight();
		w = actionBitmaps[actionTypes[0].ordinal()].getWidth();
	}
	
}
