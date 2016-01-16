package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionCellBuy;
import ru.ancientempires.action.ActionGameEndTurn;
import ru.ancientempires.action.ActionUnitAttack;
import ru.ancientempires.action.ActionUnitCapture;
import ru.ancientempires.action.ActionUnitMove;
import ru.ancientempires.action.ActionUnitRaise;
import ru.ancientempires.action.ActionUnitRepair;

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
