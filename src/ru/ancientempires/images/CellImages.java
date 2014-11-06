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
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.XMLHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.view.GameView;
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
		NodeList images = infoDocument.getElementsByTagName("image");
		
		String defaultImagesFolder = XMLHelper.getOneNode(infoDocument, "default_images_folder").getTextContent();
		String destroyingImagesFolder = XMLHelper.getOneNode(infoDocument, "destroying_images_folder").getTextContent();
		
		MyAssert.a(images.getLength() == CellType.amount);
		int typeAmount = CellType.amount;
		
		CellImages.cellsBitmaps = new CellBitmap[typeAmount];
		for (int i = 0; i < typeAmount; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			
			if ("CASTLE".equals(typeName))
				MyLog.log();
			
			CellBitmap cellBitmap = new CellBitmap();
			
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			cellBitmap.defaultBitmap = BitmapHelper.getResizeBitmap(imagesZipFile, zipPath + defaultImagesFolder + imageName);
			
			cellBitmap.changeSize();
			cellBitmap.isNormal = cellBitmap.h == GameView.baseH && cellBitmap.w == GameView.baseW;
			if (!cellBitmap.isNormal)
			{
				cellBitmap.offsetI = Integer.valueOf(XMLHelper.getNodeAttributeValue(node, "offsetI"));
				cellBitmap.offsetJ = Integer.valueOf(XMLHelper.getNodeAttributeValue(node, "offsetJ"));
			}
			
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
		
		Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
		Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "name", "value", "color_folder");
		String imagesPathR = zipPath + mapColorsFolders.get("0xFFFF0000");
		String imagesPathG = zipPath + mapColorsFolders.get("0xFF00FF00");
		String imagesPathB = zipPath + mapColorsFolders.get("0xFF0000FF");
		
		NodeList images = infoDocument.getElementsByTagName("image");
		
		MyAssert.a(images.getLength() == CellType.amount);
		int typeAmount = CellType.amount;
		int playerAmount = Player.amount;
		
		for (int i = 0; i < typeAmount; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			if (type.isCapture)
			{
				CellBitmap cellBitmap = CellImages.cellsBitmaps[i];
				cellBitmap.colorsBitmaps = new Bitmap[playerAmount];
				
				String imageName = XMLHelper.getNodeAttributeValue(node, "image");
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
					
					cellBitmap.colorsBitmaps[j] = bitmap;
				}
			}
		}
	}
	
}
