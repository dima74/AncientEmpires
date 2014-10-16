package ru.ancientempires.images;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.CellBitmap;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.ImageHelper;
import ru.ancientempires.helpers.XMLHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import android.graphics.Bitmap;

public class CellImages
{
	
	public static CellBitmap[]	cellsBitmaps;
	
	public static Bitmap getCellBitmap(Cell cell)
	{
		return CellImages.cellsBitmaps[cell.type.ordinal].getBitmap(cell);
	}
	
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		String cellsImagesFolderPath = XMLHelper.getOneTagText(imageInfoDocument, "cell_images_folder_path");
		CellImages.preloadCellsResources(imagesZipFile, cellsImagesFolderPath);
	}
	
	private static void preloadCellsResources(ZipFile imagesZipFile, String zipPath) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		NodeList images = infoDocument.getElementsByTagName("cell_image");
		
		String defaultImagesFolder = XMLHelper.getOneNode(infoDocument, "default_images_folder").getTextContent();
		String destroyingImagesFolder = XMLHelper.getOneNode(infoDocument, "destroying_images_folder").getTextContent();
		
		MyAssert.a(images.getLength() == CellType.amount);
		int length = images.getLength();
		
		CellImages.cellsBitmaps = new CellBitmap[length];
		for (int i = 0; i < length; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			
			CellBitmap cellBitmap = new CellBitmap();
			
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			cellBitmap.defaultBitmap = BitmapHelper.getResizeBitmap(imagesZipFile, zipPath + defaultImagesFolder + imageName);
			
			if (type.isDestroying)
				cellBitmap.destroyingBitmap = BitmapHelper.getResizeBitmap(imagesZipFile, zipPath + destroyingImagesFolder + imageName);
			
			CellImages.cellsBitmaps[i] = cellBitmap;
		}
	}
	
	public static void loadResources(ZipFile imagesZipFile, Game game) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		String cellsImagesFolderPath = XMLHelper.getOneTagText(imageInfoDocument, "cell_images_folder_path");
		CellImages.loadCellsResources(imagesZipFile, cellsImagesFolderPath, game);
	}
	
	private static void loadCellsResources(ZipFile imagesZipFile, String zipPath, Game game) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		NodeList images = infoDocument.getElementsByTagName("cell_image");
		
		MyAssert.a(images.getLength() == CellType.amount);
		int length = images.getLength();
		
		for (int i = 0; i < length; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			
			CellBitmap cellBitmap = CellImages.cellsBitmaps[i];
			
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			
			if (type.isCapture)
			{
				int playerAmount = Player.amount;
				cellBitmap.colorsBitmaps = new Bitmap[playerAmount];
				
				Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
				Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "name", "value", "color_folder");
				
				String imageRedPath = zipPath + mapColorsFolders.get("0xFFFF0000") + imageName;
				String imageGreenPath = zipPath + mapColorsFolders.get("0xFF00FF00") + imageName;
				String imageBluePath = zipPath + mapColorsFolders.get("0xFF0000FF") + imageName;
				
				int[][] dataRed = Images.getMatrixDataImage(imagesZipFile, imageRedPath);
				int[][] dataGreen = Images.getMatrixDataImage(imagesZipFile, imageGreenPath);
				int[][] dataBlue = Images.getMatrixDataImage(imagesZipFile, imageBluePath);
				
				for (int j = 0; j < playerAmount; j++)
				{
					int[][] data = ImageHelper.getNewImg(dataRed, dataGreen, dataBlue, Player.getPlayer(j).color);
					cellBitmap.colorsBitmaps[j] = BitmapHelper.getResizeBitmap(data);
				}
			}
		}
	}
	
}
