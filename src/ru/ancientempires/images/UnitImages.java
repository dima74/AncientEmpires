package ru.ancientempires.images;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.SomeWithBitmaps;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.ImageHelper;
import ru.ancientempires.helpers.XMLHelper;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import android.graphics.Bitmap;

public class UnitImages
{
	
	public static SomeWithBitmaps[][]	unitsBitmaps;
	public static SomeWithBitmaps[]		greyUnitsBitmaps;
	
	public static Bitmap getUnitBitmap(Unit unit)
	{
		return (unit.isTurn ? UnitImages.greyUnitsBitmaps[unit.type.ordinal] :
				UnitImages.unitsBitmaps[unit.type.ordinal][unit.player.ordinal]).getBitmap();
	}
	
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		String unitsImagesFolderPath = XMLHelper.getOneTagText(imageInfoDocument, "unit_images_folder_path");
		UnitImages.preloadUnitsResources(imagesZipFile, unitsImagesFolderPath);
	}
	
	public static void preloadUnitsResources(ZipFile imagesZipFile, String zipPath) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		NodeList colorTypes = infoDocument.getElementsByTagName("color_type");
		
		int typesLength = UnitType.amount;
		UnitImages.greyUnitsBitmaps = new SomeWithBitmaps[typesLength];
		for (int i = 0; i < typesLength; i++)
		{
			Node colorTypeNode = colorTypes.item(i);
			Map<String, String> attributes = XMLHelper.getNodeAttributesMap(colorTypeNode);
			
			int colorTypeAmountImages = Integer.valueOf(attributes.get("amountImages"));
			Bitmap[] greyUnitBitmaps = new Bitmap[colorTypeAmountImages];
			for (int k = 0; k < colorTypeAmountImages; k++)
			{
				String imageName = attributes.get("image" + k);
				greyUnitBitmaps[k] = BitmapHelper.getResizeBitmap(imagesZipFile, zipPath + "grey/" + imageName);
			}
			UnitImages.greyUnitsBitmaps[i] = new SomeWithBitmaps().setBitmaps(greyUnitBitmaps);
		}
	}
	
	public static void loadResources(ZipFile imagesZipFile, Game game) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		String unitsImagesFolderPath = XMLHelper.getOneTagText(imageInfoDocument, "unit_images_folder_path");
		UnitImages.loadUnitsResources(imagesZipFile, unitsImagesFolderPath, game);
	}
	
	private static void loadUnitsResources(ZipFile imagesZipFile, String zipPath, Game game) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		
		Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
		Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "name", "value", "color_folder");
		String imagesRedPath = zipPath + mapColorsFolders.get("0xFFFF0000");
		String imagesGreenPath = zipPath + mapColorsFolders.get("0xFF00FF00");
		String imagesBluePath = zipPath + mapColorsFolders.get("0xFF0000FF");
		
		NodeList colorTypes = infoDocument.getElementsByTagName("color_type");
		
		int typesLength = colorTypes.getLength();
		int playerLength = game.players.length;
		
		MyAssert.a(typesLength == UnitType.amount);
		
		UnitImages.unitsBitmaps = new SomeWithBitmaps[typesLength][playerLength];
		
		for (int j = 0; j < typesLength; j++)
		{
			Node colorTypeNode = colorTypes.item(j);
			Map<String, String> attributes = XMLHelper.getNodeAttributesMap(colorTypeNode);
			
			int colorTypeAmountImages = Integer.valueOf(attributes.get("amountImages"));
			for (int k = 0; k < colorTypeAmountImages; k++)
			{
				String imageName = attributes.get("image" + k);
				
				String imageRedPath = imagesRedPath + imageName;
				String imageGreenPath = imagesGreenPath + imageName;
				String imageBluePath = imagesBluePath + imageName;
				
				int[][] dataRed = Images.getMatrixDataImage(imagesZipFile, imageRedPath);
				int[][] dataGreen = Images.getMatrixDataImage(imagesZipFile, imageGreenPath);
				int[][] dataBlue = Images.getMatrixDataImage(imagesZipFile, imageBluePath);
				
				MyAssert.a(dataRed.length == dataGreen.length);
				MyAssert.a(dataRed.length == dataBlue.length);
				MyAssert.a(dataRed[0].length == dataGreen[0].length);
				MyAssert.a(dataRed[0].length == dataBlue[0].length);
				
				for (int i = 0; i < playerLength; i++)
				{
					int color = game.players[i].color;
					int[][] data = ImageHelper.getNewImg(dataRed, dataGreen, dataBlue, color);
					Bitmap bitmap = BitmapHelper.getResizeBitmap(data);
					if (k == 0)
					{
						int jType = UnitType.getType(attributes.get("type")).ordinal;
						UnitImages.unitsBitmaps[jType][i] = new SomeWithBitmaps().setAmount(colorTypeAmountImages);
					}
					UnitImages.unitsBitmaps[j][i].setBitmaps(k, bitmap);
				}
			}
		}
		
	}
	
}
