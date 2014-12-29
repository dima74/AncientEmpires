package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.action.ActionType;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.view.OldGameView;
import android.graphics.Bitmap;

public class ActionImages
{
	
	private static Bitmap[]	actionBitmaps;
	
	public static Bitmap getActionBitmap(ActionType type)
	{
		return ActionImages.actionBitmaps[type.ordinal];
	}
	
	public static void preloadResources(ZipFile images, String path) throws IOException
	{
		ActionImages.actionBitmaps = new Bitmap[ActionType.amount];
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
			ActionImages.actionBitmaps[actionTypes[i].ordinal] = BitmapHelper.
					getMultiBitmap(images, path + actionImageNames[i], OldGameView.baseMulti * 1.5f);
	}
	
}
