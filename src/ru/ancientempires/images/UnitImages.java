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
	
	public static SomeWithBitmaps[][]	unitBitmaps;
	
	public static Bitmap getUnitBitmap(Unit unit)
	{
		return UnitImages.unitBitmaps[unit.type.ordinal][unit.player.ordinal].getBitmap();
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
		
		NodeList colorTypes = infoDocument.getElementsByTagName("color_type");
		
		MyAssert.a(colorTypes.getLength() == UnitType.amount);
		
		int typesLength = colorTypes.getLength();
		int playerLength = game.players.length;
		
		UnitImages.unitBitmaps = new SomeWithBitmaps[typesLength][playerLength];
		
		for (int j = 0; j < typesLength; j++)
		{
			Node colorTypeNode = colorTypes.item(j);
			Map<String, String> attributes = XMLHelper.getNodeAttributesMap(colorTypeNode);
			
			int colorTypeAmountImages = Integer.valueOf(attributes.get("amountImages"));
			for (int k = 0; k < colorTypeAmountImages; k++)
			{
				String imageName = attributes.get("image" + k);
				
				String imageRedPath = zipPath + mapColorsFolders.get("0xFFFF0000") + imageName;
				String imageGreenPath = zipPath + mapColorsFolders.get("0xFF00FF00") + imageName;
				String imageBluePath = zipPath + mapColorsFolders.get("0xFF0000FF") + imageName;
				
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
						UnitImages.unitBitmaps[jType][i] = new SomeWithBitmaps().setAmount(colorTypeAmountImages);
					}
					UnitImages.unitBitmaps[j][i].setBitmaps(k, bitmap);
				}
			}
		}
		
	}
	
}
