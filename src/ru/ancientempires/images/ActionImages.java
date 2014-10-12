package ru.ancientempires.images;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;

import ru.ancientempires.action.ActionType;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.XMLHelper;
import android.graphics.Bitmap;

public class ActionImages
{
	
	private static Map<ActionType, Bitmap>	actionBitmaps	= new HashMap<ActionType, Bitmap>();
	
	public static Bitmap getActionBitmap(ActionType type)
	{
		return ActionImages.actionBitmaps.get(type);
	}
	
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		String imagesFolderPath = XMLHelper.getOneTagText(imageInfoDocument, "actions_images_folder_path");
		
		String[] actionImageNames = new String[]
		{
				"action_cell_buy.png",
				"action_unit_move.png",
				"action_unit_capture.png",
				"action_unit_capture.png",
				"action_unit_attack.png",
				"action_unit_raise.png",
				"action_game_end_turn.png",
		};
		
		ActionType[] actionTypes = new ActionType[]
		{
				ActionType.ACTION_CELL_BUY,
				ActionType.ACTION_UNIT_MOVE,
				ActionType.ACTION_UNIT_REPAIR,
				ActionType.ACTION_UNIT_CAPTURE,
				ActionType.ACTION_UNIT_ATTACK,
				ActionType.ACTION_UNIT_RAISE,
				ActionType.ACTION_GAME_END_TURN
		};
		
		for (int i = 0; i < actionImageNames.length; i++)
		{
			Bitmap bitmap = BitmapHelper.getBitmap(imagesZipFile, imagesFolderPath + actionImageNames[i], 4, 4);
			ActionImages.actionBitmaps.put(actionTypes[i], bitmap);
		}
	}
	
	// Это слишком сложный способ
	/*
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		String imagesFolderPath = XMLHelper.getOneTagText(imageInfoDocument, "actions_images_folder_path");
		
		ActionImages.actionsBitmaps = new Bitmap[ActionType.userAmount];
		int k = 0;
		ActionType[] types = ActionType.types;
		for (ActionType type : types)
			if (type.user)
			{
				Bitmap bitmap = BitmapHelper.getBitmap(imagesZipFile, imagesFolderPath+type.????);
				ActionImages.actionsBitmaps[k] = bitmap;
				k++;
			}
	}
	*/
	
	// А это ещё сложней
	/*
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		String imagesFolderPath = XMLHelper.getOneTagText(imageInfoDocument, "actions_images_folder_path");
		ActionImages.preloadActionsResources(imagesZipFile, imagesFolderPath);
	}
	
	private static void preloadActionsResources(ZipFile imagesZipFile, String zipPath) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		Node imagesNode = XMLHelper.getOneNode(infoDocument, "action_images");
		Map<String, String> imagesMap = XMLHelper.getMapFromNode(imagesNode, "type", "image", "action_image");
		
		ActionImages.actionsBitmaps = new Bitmap[ActionType.userAmount];
		MyAssert.a(imagesMap.entrySet().size() == ActionType.userAmount);
		for (Entry<String, String> entry : imagesMap.entrySet())
		{
			String type=entry.getKey();
		}
	}
	*/
	
}
