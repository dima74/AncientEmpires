package ru.ancientempires.images;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.CellBitmap;
import ru.ancientempires.activity.MainActivity;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.XMLHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import android.graphics.Bitmap;

public class CellImages
{
	
	private static CellBitmap[]	cellBitmaps;
	private static CellBitmap[]	cellBitmapsDual;
	
	public static Bitmap getCellBitmap(Cell cell, boolean dual)
	{
		CellBitmap cellBitmap = CellImages.cellBitmaps[cell.type.ordinal];
		return dual ?
				cellBitmap.isDual ?
						CellImages.cellBitmapsDual[cell.type.ordinal].getBitmap(cell)
						:
						null
				:
				cellBitmap.getBitmap(cell);
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
		NodeList images = infoDocument.getElementsByTagName("image");
		
		String defaultImagesFolder = XMLHelper.getOneNode(infoDocument, "default_images_folder").getTextContent();
		String destroyingImagesFolder = XMLHelper.getOneNode(infoDocument, "destroying_images_folder").getTextContent();
		
		MyAssert.a(images.getLength() == CellType.amount);
		int typeAmount = CellType.amount;
		
		CellImages.cellBitmaps = new CellBitmap[typeAmount];
		CellImages.cellBitmapsDual = new CellBitmap[typeAmount];
		for (int i = 0; i < typeAmount; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			
			CellBitmap cellBitmap = new CellBitmap();
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			cellBitmap.defaultBitmap = BitmapHelper.getResizeBitmap(imagesZipFile, zipPath + defaultImagesFolder + imageName);
			
			if ("true".equals(XMLHelper.getNodeAttributesMap(node).get("isDual")))
			{
				cellBitmap.isDual = true;
				CellBitmap cellBitmapDual = new CellBitmap();
				String imageNameDual = XMLHelper.getNodeAttributeValue(node, "imageDual");
				cellBitmapDual.defaultBitmap = BitmapHelper.getResizeBitmap(imagesZipFile, zipPath + defaultImagesFolder + imageNameDual);
				CellImages.cellBitmapsDual[i] = cellBitmapDual;
			}
			
			if (type.isDestroying)
				cellBitmap.destroyingBitmap = BitmapHelper.getResizeBitmap(imagesZipFile, zipPath + destroyingImagesFolder + imageName);
			
			CellImages.cellBitmaps[i] = cellBitmap;
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
		
		Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
		Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "name", "value", "color_folder");
		String imagesPathR = zipPath + mapColorsFolders.get("0xFFFF0000");
		String imagesPathG = zipPath + mapColorsFolders.get("0xFF00FF00");
		String imagesPathB = zipPath + mapColorsFolders.get("0xFF0000FF");
		
		NodeList images = infoDocument.getElementsByTagName("image");
		
		MyAssert.a(images.getLength() == CellType.amount);
		int typeAmount = CellType.amount;
		
		for (int i = 0; i < typeAmount; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			if (type.isCapture)
			{
				CellBitmap cellBitmap = CellImages.cellBitmaps[i];
				String imageName = XMLHelper.getNodeAttributeValue(node, "image");
				cellBitmap.colorsBitmaps = CellImages.loadOneColorBitmap(imagesZipFile, game,
						imagesPathR, imagesPathG, imagesPathB, imageName);
				
				if (cellBitmap.isDual)
				{
					CellBitmap cellBitmapDual = CellImages.cellBitmapsDual[i];
					String imageNameDual = XMLHelper.getNodeAttributeValue(node, "imageDual");
					cellBitmapDual.colorsBitmaps = CellImages.loadOneColorBitmap(imagesZipFile,
							game, imagesPathR, imagesPathG, imagesPathB, imageNameDual);
				}
			}
		}
	}
	
	private static Bitmap[] loadOneColorBitmap(ZipFile imagesZipFile, Game game, String imagesPathR, String imagesPathG, String imagesPathB, String imageName)
			throws IOException
	{
		Bitmap[] bitmaps = new Bitmap[Player.amount];
		
		RenderScriptCellImages rs = new RenderScriptCellImages();
		rs.createScript(MainActivity.context);
		
		Bitmap bitmapR = BitmapHelper.getBitmap(imagesZipFile, imagesPathR + imageName);
		Bitmap bitmapG = BitmapHelper.getBitmap(imagesZipFile, imagesPathG + imageName);
		Bitmap bitmapB = BitmapHelper.getBitmap(imagesZipFile, imagesPathB + imageName);
		rs.setBitmaps(bitmapR, bitmapG, bitmapB);
		
		for (int j = 0; j < Player.amount; j++)
		{
			Bitmap bitmap = rs.getAssociationBitmap(game.players[j].color);
			bitmap = BitmapHelper.getResizeBitmap(bitmap);
			bitmaps[j] = bitmap;
		}
		return bitmaps;
	}
	
}