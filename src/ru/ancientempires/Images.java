package ru.ancientempires;

import helpers.XMLHelper;
import helpers.ZIPHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipFile;

import model.CellType;
import model.Game;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Images
{
	
	public static Bitmap[][]	unitBitmaps;
	
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
			
			InputStream inputStream = ZIPHelper.getISFromZipFile(imagesZipFile, zipPath + imageName);
			view.bitmap = BitmapHelper.getResizeBitmap(BitmapFactory.decodeStream(inputStream));
		}
	}
	
	public static void loadUnitsResources(ZipFile imagesZipFile, String zipPath, Game game) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		
		Node colorFoldersNode = XMLHelper.getOneNode(infoDocument, "color_folders");
		Map<String, String> mapColorsFolders = XMLHelper.getMapFromNode(colorFoldersNode, "color_folder");
		Вот тут я в прошлый раз закончил
		и не вздумай комментить эту строку
		и эту
		и еще 7
		1
		2
		3
		4
		5
		6
		7
		
		System.out.println();
		
		// Node colorFoldersNode = XMLHelper.
		// Map<String, String> mapColors= XMLHelper.getMapFromNode(node, namePartNodes)
		
		/*
		
		NodeList images = infoDocument.getElementsByTagName("unit_image");
		
		int length = images.getLength();
		UnitView.setCount(length);
		for (int i = 0; i < length; i++)
		{
			Node node = images.item(i);
			String typeName = XMLHelper.getNodeAttributeValue(node, "type");
			int amountImages = Integer.valueOf(XMLHelper.getNodeAttributeValue(node, "amountImages"));
			
			Bitmap[] bitmaps = new Bitmap[amountImages];
			for (int j = 0; j < amountImages; j++)
			{
				String imageName = XMLHelper.getNodeAttributeValue(node, "image" + j);
				InputStream inputStream = ZIPHelper.getISFromZipFile(imagesZipFile, zipPath + imageName);
				bitmaps[j] = BitmapHelper.getResizeBitmap(BitmapFactory.decodeStream(inputStream));
			}
			
			UnitView view = new UnitView(UnitType.getType(typeName));
			view.bitmaps = bitmaps;
		}
		*/
	}
}
