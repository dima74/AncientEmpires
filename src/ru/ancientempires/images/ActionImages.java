package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.action.ActionType;

public class ActionImages extends IImages
{
	
	private Bitmap[]	actionBitmaps;
	public int			h;
	public int			w;
	
	public Bitmap getActionBitmap(ActionType type)
	{
		return actionBitmaps[type.ordinal];
	}
	
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		actionBitmaps = new Bitmap[ActionType.amount];
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
		
		ActionType[] actionTypes = new ActionType[]
		{
				ActionType.ACTION_CELL_BUY,
				ActionType.ACTION_UNIT_MOVE,
				ActionType.ACTION_UNIT_REPAIR,
				ActionType.ACTION_UNIT_CAPTURE,
				ActionType.ACTION_UNIT_ATTACK,
				ActionType.ACTION_UNIT_RAISE,
				ActionType.ACTION_END_TURN
		};
		
		for (int i = 0; i < actionImageNames.length; i++)
			actionBitmaps[actionTypes[i].ordinal] = loader.loadImageAndResize(actionImageNames[i], 2.5f);
		h = actionBitmaps[actionTypes[0].ordinal].getHeight();
		w = actionBitmaps[actionTypes[0].ordinal].getWidth();
	}
	
}
