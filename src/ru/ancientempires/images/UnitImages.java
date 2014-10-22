package ru.ancientempires.images;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.MainActivity;
import ru.ancientempires.SomeWithBitmaps;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.XMLHelper;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
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
	
	private static void preloadUnitsResources(ZipFile imagesZipFile, String zipPath) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		NodeList colorTypes = infoDocument.getElementsByTagName("image");
		
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
		String imagesPathR = zipPath + mapColorsFolders.get("0xFFFF0000");
		String imagesPathG = zipPath + mapColorsFolders.get("0xFF00FF00");
		String imagesPathB = zipPath + mapColorsFolders.get("0xFF0000FF");
		
		NodeList images = infoDocument.getElementsByTagName("image");
		
		MyAssert.a(images.getLength() == UnitType.amount);
		int typeAmount = UnitType.amount;
		int playerAmount = Player.amount;
		
		UnitImages.unitsBitmaps = new SomeWithBitmaps[typeAmount][playerAmount];
		
		for (int i = 0; i < typeAmount; i++)
		{
			Node node = images.item(i);
			Map<String, String> attributes = XMLHelper.getNodeAttributesMap(node);
			String nameType = attributes.get("type");
			int iType = UnitType.getType(nameType).ordinal;
			
			int amountImages = Integer.valueOf(attributes.get("amountImages"));
			for (int k = 0; k < amountImages; k++)
			{
				String imageName = attributes.get("image" + k);
				
				String imagePathR = imagesPathR + imageName;
				String imagePathG = imagesPathG + imageName;
				String imagePathB = imagesPathB + imageName;
				
				/*
				int[][] dataR = Images.getMatrixDataImage(imagesZipFile, imagePathR);
				int[][] dataG = Images.getMatrixDataImage(imagesZipFile, imagePathG);
				int[][] dataB = Images.getMatrixDataImage(imagesZipFile, imagePathB);
				// */
				
				// *
				RenderScriptCellImages rs = new RenderScriptCellImages();
				rs.createScript(MainActivity.context);
				
				Bitmap bitmapR = BitmapHelper.getBitmap(imagesZipFile, imagePathR);
				Bitmap bitmapG = BitmapHelper.getBitmap(imagesZipFile, imagePathG);
				Bitmap bitmapB = BitmapHelper.getBitmap(imagesZipFile, imagePathB);
				rs.setBitmaps(bitmapR, bitmapG, bitmapB);
				// */
				
				for (int j = 0; j < playerAmount; j++)
				{
					// int[][] data = ImageHelper.getNewImg(dataR, dataG, dataB, game.players[j].color);
					// Bitmap bitmap = BitmapHelper.getResizeBitmap(data);
					
					Bitmap bitmap = rs.getAssociationBitmap(game.players[j].color);
					bitmap = BitmapHelper.getResizeBitmap(bitmap);
					
					if (k == 0)
						UnitImages.unitsBitmaps[iType][j] = new SomeWithBitmaps().setAmount(amountImages);
					UnitImages.unitsBitmaps[i][j].setBitmaps(k, bitmap);
				}
			}
		}
		
	}
}
