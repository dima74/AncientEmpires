package ru.ancientempires;

import helpers.ImageHelper;
import helpers.XMLHelper;
import helpers.ZIPHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.zip.ZipFile;

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
		return Images.unitBitmaps[unit.player.ordinal][unit.type.ordinal].getBitmap();
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
		
		for (int i = 0; i < images.getLength(); i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			String imageName = XMLHelper.getNodeAttributeValue(node, "image");
			CellView view = new CellView(CellType.getType(typeName));
			
			InputStream inputStream = ZIPHelper.getIS(imagesZipFile, zipPath + imageName);
			view.bitmap = BitmapHelper.getResizeBitmap(BitmapFactory.decodeStream(inputStream));
		}
	}
	
	public static void loadUnitsResources(ZipFile imagesZipFile, String zipPath, Game game) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		
		Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
		Node colorTypesNode = XMLHelper.getOneNode(infoDocument, "color_types");
		Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "color_folder");
		// Map<String, String> mapColorTypes = XMLHelper.getMapFromNode(colorTypesNode, "color_type");
		
		NodeList colorTypes = infoDocument.getElementsByTagName("color_type");
		
		assert colorTypes.getLength() == UnitType.types.length;
		
		int typesLength = colorTypes.getLength();
		int playerLength = game.players.length;
		
		Images.unitBitmaps = new SomeWithBitmaps[playerLength][typesLength];
		
		for (int j = 0; j < typesLength; j++)
		{
			Node colorTypeNode = colorTypes.item(j);
			Map<String, String> attributes = XMLHelper.getNodeAttributesMap(colorTypeNode);
			
			String type = attributes.get("type");
			UnitType unitType = UnitType.getType(type);
			int colorTypeAmountImages = Integer.valueOf(attributes.get("amountImages"));
			// String[] colorTypeImages = new String[colorTypeAmountImages];
			for (int k = 0; k < colorTypeAmountImages; k++)
			{
				String typeImagePath = attributes.get("image" + k);
				
				String imageRedPath = zipPath + "red/" + typeImagePath;
				String imageGreenPath = zipPath + "green/" + typeImagePath;
				String imageBluePath = zipPath + "blue/" + typeImagePath;
				
				int[][] dataRed = Images.getMatrixDataImage(imagesZipFile, imageRedPath);
				int[][] dataGreen = Images.getMatrixDataImage(imagesZipFile, imageGreenPath);
				int[][] dataBlue = Images.getMatrixDataImage(imagesZipFile, imageBluePath);
				
				assert dataRed.length == dataGreen.length;
				assert dataRed.length == dataBlue.length;
				assert dataRed[0].length == dataGreen[0].length;
				assert dataRed[0].length == dataBlue[0].length;
				
				for (int i = 0; i < playerLength; i++)
				{
					// Images.unitBitmaps[i][j] = new SomeWithBitmaps();
					int color = game.players[i].color;
					int[][] data = ImageHelper.getNewImg(dataRed, dataGreen, dataBlue, color);
					int[] dataArray = Images.getArrayFromMatrix(data);
					
					Bitmap bitmap = Bitmap.createBitmap(dataArray, data[0].length, data.length, Config.ARGB_8888);
					bitmap = BitmapHelper.getResizeBitmap(bitmap);
					if (k == 0) Images.unitBitmaps[i][j] = new SomeWithBitmaps().setAmount(colorTypeAmountImages);
					Images.unitBitmaps[i][j].setBitmaps(k, bitmap);
				}
			}
		}
		
		System.out.println();
	}
	
	/*
	public static byte[] intArrayToByteArray(int[] array)
	{
		byte[] result = new byte[array.length * 4];
		
		for (int i = 0; i < array.length; i++)
		{
			int color = array[i];
			color = ImageHelper.toBitmapColor(color);
			result[i * 4 + 0] = (byte) ImageHelper.getA(color);
			result[i * 4 + 1] = (byte) ImageHelper.getR(color);
			result[i * 4 + 2] = (byte) ImageHelper.getG(color);
			result[i * 4 + 3] = (byte) ImageHelper.getB(color);
		}
		
		return result;
	}
	*/
	
	public static int[][] getMatrixDataImage(ZipFile imagesZipFile, String path) throws IOException
	{
		Bitmap bitmap = BitmapFactory.decodeStream(ZIPHelper.getIS(imagesZipFile, path));
		
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		IntBuffer buffer = IntBuffer.allocate(height * width);
		bitmap.copyPixelsToBuffer(buffer);
		int[] dataArray = buffer.array();
		
		int[][] data = Images.getMatrixFromArray(dataArray, height, width);
		
		// TODO если вот тут BitmapFactory.decodeStream в Options поставить inPreMultiPlied (API 19) в false, то вроде можно без костыля
		Images.costil(data);
		
		return data;
	}
	
	private static void costil(int[][] data)
	{
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[0].length; j++)
				data[i][j] = ImageHelper.toNormalColor(data[i][j]);
	}
	
	public static int[][] getMatrixFromArray(int[] array, int height, int width)
	{
		int[][] matrix = new int[height][width];
		
		int k = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				matrix[i][j] = array[k];
				k++;
			}
		
		return matrix;
	}
	
	public static int[] getArrayFromMatrix(int[][] matrix)
	{
		int height = matrix.length;
		int width = matrix[0].length;
		int[] array = new int[height * width];
		
		int k = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
			{
				array[k] = matrix[i][j];
				k++;
			}
		
		return array;
	}
	
}
