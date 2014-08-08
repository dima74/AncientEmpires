package ru.ancientempires;

import helpers.XMLHelper;
import helpers.ZIPHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import model.CellType;
import model.UnitType;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageLoader
{
	
	public static void loadResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		
		String cellsImagesFolderPath = XMLHelper.getOneTagNameFromDocument(imageInfoDocument, "cell_images_folder_path");
		ImageLoader.loadCellsResources(imagesZipFile, cellsImagesFolderPath);
		String unitsImagesFolderPath = XMLHelper.getOneTagNameFromDocument(imageInfoDocument, "unit_images_folder_path");
		ImageLoader.loadUnitsResources(imagesZipFile, unitsImagesFolderPath);
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
	
	public static void loadUnitsResources(ZipFile imagesZipFile, String zipPath) throws IOException
	{
		Document infoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, zipPath + "info.xml");
		
		NodeList images = infoDocument.getElementsByTagName("unit_image");
		
		for (int i = 0; i < images.getLength(); i++)
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
		
	}
	
}
