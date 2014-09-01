package ru.ancientempires;

import helpers.ArrayHelper;
import helpers.ColorHelper;
import helpers.ImageHelper;
import helpers.MatrixHelper;
import helpers.XMLHelper;
import helpers.ZIPHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.zip.ZipFile;

import model.Cell;
import model.CellType;
import model.Game;
import model.Unit;
import model.UnitType;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class Images
{
	
	public static SomeWithBitmaps[][]	unitBitmaps;
	
	public static Bitmap getUnitBitmap(Unit unit)
	{
		return Images.unitBitmaps[unit.type.ordinal][unit.player.ordinal].getBitmap();
	}
	
	public static SomeWithBitmaps[][]	cellsBitmaps;
	public static Bitmap[]				cellsStaticBitmaps;
	
	public static Bitmap getCellBitmap(Cell cell)
	{
		CellType type = cell.type;
		if (type.isStatic())
			return Images.cellsStaticBitmaps[type.staticOrdinal];
		else
			return null;
	}
	
	public static void loadResources(ZipFile imagesZipFile, Game game) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		
		String cellsImagesFolderPath = XMLHelper.getOneTagNameFromDocument(imageInfoDocument, "cell_images_folder_path");
		String unitsImagesFolderPath = XMLHelper.getOneTagNameFromDocument(imageInfoDocument, "unit_images_folder_path");
		
		Images.loadCellsResources(imagesZipFile, cellsImagesFolderPath);
		Images.loadUnitsResources(imagesZipFile, unitsImagesFolderPath, game);
	}
	
	public static void loadCellsResources(ZipFile imagesZipFile, String zipPath) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		NodeList images = infoDocument.getElementsByTagName("cell_image");
		
		Images.cellsStaticBitmaps = new Bitmap[CellType.staticAmount];
		
		for (int i = 0; i < images.getLength(); i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			CellType type = CellType.getType(typeName);
			// CellView view = new CellView(type);
			
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			
			InputStream inputStream = ZIPHelper.getIS(imagesZipFile, zipPath + imageName);
			Bitmap bitmap = BitmapHelper.getResizeBitmap(BitmapFactory.decodeStream(inputStream));
			
			Images.cellsStaticBitmaps[type.staticOrdinal] = bitmap;
			
			// view.bitmap = bitmap;
		}
	}
	
	public static void loadUnitsResources(ZipFile imagesZipFile, String zipPath, Game game) throws IOException
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
				String typeImagePath = attributes.get("image" + k);
				
				String imageRedPath = zipPath + mapColorsFolders.get("0xFFFF0000") + typeImagePath;
				String imageGreenPath = zipPath + mapColorsFolders.get("0xFF00FF00") + typeImagePath;
				String imageBluePath = zipPath + mapColorsFolders.get("0xFF0000FF") + typeImagePath;
				
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
					int[] dataArray = MatrixHelper.getArrayFromMatrix(data);
					
					Bitmap bitmap = Bitmap.createBitmap(dataArray, data[0].length, data.length, Config.ARGB_8888);
					bitmap = BitmapHelper.getResizeBitmap(bitmap);
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
