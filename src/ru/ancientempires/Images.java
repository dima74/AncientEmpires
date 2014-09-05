package ru.ancientempires;

import helpers.ArrayHelper;
import helpers.ColorHelper;
import helpers.ImageHelper;
import helpers.XMLHelper;
import helpers.ZIPHelper;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.zip.ZipFile;

import model.Cell;
import model.CellType;
import model.Game;
import model.Player;
import model.Unit;
import model.UnitType;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Images
{
	
	public static SomeWithBitmaps[][]	unitBitmaps;
	
	public static Bitmap getUnitBitmap(Unit unit)
	{
		return Images.unitBitmaps[unit.type.ordinal][unit.player.ordinal].getBitmap();
	}
	
	public static CellBitmap[]	cellsBitmaps;
	
	// public static Bitmap[] cellsStaticBitmaps;
	
	public static Bitmap getCellBitmap(Cell cell)
	{
		return Images.cellsBitmaps[cell.type.ordinal].getBitmap(cell);
	}
	
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		
		String cellsImagesFolderPath = XMLHelper.getOneTagNameFromDocument(imageInfoDocument, "cell_images_folder_path");
		
		Images.preloadCellsResources(imagesZipFile, cellsImagesFolderPath);
	}
	
	public static void preloadCellsResources(ZipFile imagesZipFile, String zipPath) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		NodeList images = infoDocument.getElementsByTagName("cell_image");
		
		String defaultImagesFolder = XMLHelper.getOneNode(infoDocument, "default_images_folder").getTextContent();
		String destroyingImagesFolder = XMLHelper.getOneNode(infoDocument, "destroying_images_folder").getTextContent();
		
		/*
		Images.cellsStaticBitmaps = new Bitmap[CellType.staticAmount];
		for (int i = 0; i < images.getLength(); i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			Bitmap bitmap = BitmapHelper.getBitmap(imagesZipFile, zipPath + imageName);
			
			Images.cellsStaticBitmaps[type.staticOrdinal] = bitmap;
		}
		*/
		
		assert images.getLength() == CellType.amount;
		int length = images.getLength();
		
		Images.cellsBitmaps = new CellBitmap[length];
		for (int i = 0; i < length; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			
			CellBitmap cellBitmap = new CellBitmap();
			
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			cellBitmap.defaultBitmap = BitmapHelper.getBitmap(imagesZipFile, zipPath + defaultImagesFolder + imageName);
			
			if (type.isDestroying)
			{
				String destroyingImageName = XMLHelper.getNodeAttributeValue(node, "destroyingImage");
				cellBitmap.destroyingBitmap = BitmapHelper.getBitmap(imagesZipFile, zipPath + destroyingImagesFolder + imageName);
			}
			
			Images.cellsBitmaps[i] = cellBitmap;
		}
	}
	
	public static void loadResources(ZipFile imagesZipFile, Game game) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		
		String cellsImagesFolderPath = XMLHelper.getOneTagNameFromDocument(imageInfoDocument, "cell_images_folder_path");
		String unitsImagesFolderPath = XMLHelper.getOneTagNameFromDocument(imageInfoDocument, "unit_images_folder_path");
		
		Images.loadCellsResources(imagesZipFile, cellsImagesFolderPath, game);
		Images.loadUnitsResources(imagesZipFile, unitsImagesFolderPath, game);
	}
	
	private static void loadCellsResources(ZipFile imagesZipFile, String zipPath, Game game) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		NodeList images = infoDocument.getElementsByTagName("cell_image");
		
		assert images.getLength() == CellType.amount;
		int length = images.getLength();
		
		for (int i = 0; i < length; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			
			CellBitmap cellBitmap = Images.cellsBitmaps[i];
			
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			
			if (type.isCapture)
			{
				int playerLength = Player.players.length;
				cellBitmap.colorsBitmaps = new Bitmap[playerLength];
				
				Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
				Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "color_folder");
				
				String imageRedPath = zipPath + mapColorsFolders.get("0xFFFF0000") + imageName;
				String imageGreenPath = zipPath + mapColorsFolders.get("0xFF00FF00") + imageName;
				String imageBluePath = zipPath + mapColorsFolders.get("0xFF0000FF") + imageName;
				
				int[][] dataRed = Images.getMatrixDataImage(imagesZipFile, imageRedPath);
				int[][] dataGreen = Images.getMatrixDataImage(imagesZipFile, imageGreenPath);
				int[][] dataBlue = Images.getMatrixDataImage(imagesZipFile, imageBluePath);
				
				for (int j = 0; j < playerLength; j++)
				{
					int[][] data = ImageHelper.getNewImg(dataRed, dataGreen, dataBlue, Player.players[j].color);
					cellBitmap.colorsBitmaps[j] = BitmapHelper.getResizeBitmap(data);
				}
			}
		}
	}
	
	private static void loadUnitsResources(ZipFile imagesZipFile, String zipPath, Game game) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		
		Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
		Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "color_folder");
		
		NodeList colorTypes = infoDocument.getElementsByTagName("color_type");
		
		assert colorTypes.getLength() == UnitType.types.length;
		
		int typesLength = colorTypes.getLength();
		int playerLength = game.players.length;
		
		Images.unitBitmaps = new SomeWithBitmaps[typesLength][playerLength];
		
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
				
				assert dataRed.length == dataGreen.length;
				assert dataRed.length == dataBlue.length;
				assert dataRed[0].length == dataGreen[0].length;
				assert dataRed[0].length == dataBlue[0].length;
				
				for (int i = 0; i < playerLength; i++)
				{
					int color = game.players[i].color;
					int[][] data = ImageHelper.getNewImg(dataRed, dataGreen, dataBlue, color);
					Bitmap bitmap = BitmapHelper.getResizeBitmap(data);
					if (k == 0)
					{
						int jType = UnitType.getType(attributes.get("type")).ordinal;
						Images.unitBitmaps[jType][i] = new SomeWithBitmaps().setAmount(colorTypeAmountImages);
					}
					Images.unitBitmaps[j][i].setBitmaps(k, bitmap);
				}
			}
		}
		
		System.out.println();
	}
	
	public static int[][] getMatrixDataImage(ZipFile imagesZipFile, String path) throws IOException
	{
		Bitmap bitmap = BitmapFactory.decodeStream(ZIPHelper.getIS(imagesZipFile, path));
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		IntBuffer buffer = IntBuffer.allocate(height * width);
		bitmap.copyPixelsToBuffer(buffer);
		int[] dataArray = buffer.array();
		
		int[][] data = ArrayHelper.getMatrixFromArray(dataArray, height, width);
		
		// TODO если вот тут BitmapFactory.decodeStream в Options поставить inPreMultiPlied (API 19) в false, то вроде можно без костыля
		Images.costil(data);
		
		return data;
	}
	
	private static void costil(int[][] data)
	{
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[0].length; j++)
				data[i][j] = ColorHelper.toNormalColor(data[i][j]);
	}
	
}
