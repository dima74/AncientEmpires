package ru.ancientempires;

import helpers.XMLHelper;
import helpers.ZIPHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import model.CellType;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.BitmapFactory;

public class ImageLoader
{
	
	public static void initResources(ZipFile imagesZipFile) throws IOException
	{
		Document imageInfoDocument = XMLHelper.getDocumentFromZipPath(imagesZipFile, "info.xml");
		NodeList cells = imageInfoDocument.getElementsByTagName("cell");
		
		for (int i = 0; i < cells.getLength(); i++)
		{
			Node cell = cells.item(i);
			String cellTypeName = XMLHelper.getNodeAttributeValue(cell, "type");
			String cellImageName = XMLHelper.getNodeAttributeValue(cell, "image");
			CellView cellView = new CellView(CellType.getCellType(cellTypeName));
			
			InputStream imageInputStream = ZIPHelper.getISFromZipFile(imagesZipFile, cellImageName);
			/*
			File cachePath = Environment.getDownloadCacheDirectory();
			File cacheImageFile = new File(cachePath, cellImageName);
			FileOutputStream fileOutputStream = new FileOutputStream(cacheImageFile);
			int b;
			while ((b = imageInputStream.read()) != -1)
				fileOutputStream.write(b);
			fileOutputStream.close();
			
			cellView.fileImage = cacheImageFile;
			*/
			
			cellView.bitmap = BitmapHelper.getResizeBitmap(BitmapFactory.decodeStream(imageInputStream));
		}
	}
}
