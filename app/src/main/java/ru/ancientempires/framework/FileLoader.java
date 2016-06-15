package ru.ancientempires.framework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import ru.ancientempires.client.Client;
import ru.ancientempires.client.IClientHelper;
import ru.ancientempires.helpers.AssetsHelper;

public class FileLoader
{
	
	public AssetsHelper assets;
	public File         baseDirectory;
	public String       prefix;

	public FileLoader(IClientHelper helper)
	{
		baseDirectory = helper.getFilesDir();
		assets = helper.getAssets();
		prefix = "";
	}
	
	public FileLoader(FileLoader loader)
	{
		this(loader, "");
	}
	
	public FileLoader(FileLoader loader, String prefix)
	{
		baseDirectory = loader.baseDirectory;
		assets = loader.assets;
		this.prefix = loader.prefix + prefix;
		if (!this.prefix.endsWith("/"))
			this.prefix += "/";
	}
	
	public File getFile(String name)
	{
		return new File(baseDirectory, prefix + name);
	}
	
	public boolean exists(String name)
	{
		return getFile(name).exists() || assets.exists(prefix + name);
	}
	
	public InputStream openIS(String name) throws IOException
	{
		if (getFile(name).exists())
			return new FileInputStream(getFile(name));
		else
			return assets.openIS(prefix + name);
	}
	
	public DataInputStream openDIS(String name) throws IOException
	{
		return new DataInputStream(openIS(name));
	}

	public FileInputStream openFIS(String name) throws IOException
	{
		return new FileInputStream(getFile(name));
	}
	
	public JsonReader getReader(String name) throws IOException
	{
		if (getFile(name).exists())
			return new JsonReader(new FileReader(getFile(name)));
		else
			return new JsonReader(new InputStreamReader(openIS(name)));
	}

	public Scanner getScanner(String name) throws IOException
	{
		return new Scanner(openIS(name));
	}
	
	public void mkdirs(String name)
	{
		getFile(name).mkdirs();
	}
	
	public void mkdirs()
	{
		mkdirs("");
	}
	
	public OutputStream openOS(String name) throws IOException
	{
		return new FileOutputStream(getFile(name));
	}
	
	public DataOutputStream openDOS(String name) throws IOException
	{
		return new DataOutputStream(openOS(name));
	}

	public FileOutputStream openFOS(String name) throws IOException
	{
		return new FileOutputStream(getFile(name));
	}

	public FileOutputStream openFOS(String name, boolean append) throws IOException
	{
		return new FileOutputStream(getFile(name), append);
	}

	public JsonWriter getWriter(String name) throws IOException
	{
		getFile(name).getParentFile().mkdirs();
		return new JsonWriter(new OutputStreamWriter(new FileOutputStream(getFile(name))));
	}

	public PrintWriter getPrintWriter(String name) throws FileNotFoundException
	{
		return new PrintWriter(getFile(name));
	}

	public PrintWriter getPrintWriter(String name, boolean append) throws IOException
	{
		return new PrintWriter(new FileWriter(getFile(name), append));
	}
	
	public FileLoader getLoader(String name)
	{
		return new FileLoader(this, name);
	}

	public void loadLocalization() throws IOException
	{
		Client.client.localization.loadFull(this);
	}
	
	public String[] list(String name) throws IOException
	{
		ArrayList<String> list = new ArrayList<>();
		String[] list1 = getFile(name).list();
		if (list1 != null)
			list.addAll(Arrays.asList(list1));
		String[] list2 = assets.list(prefix + name);
		if (list2 != null)
			list.addAll(Arrays.asList(list2));
		return list.toArray(new String[0]);
	}

	public Bitmap loadImage(String name) throws IOException
	{
		return BitmapFactory.decodeStream(openIS(name));
	}

	public Bitmap loadImageAndResize(String name, float scale) throws IOException
	{
		Bitmap bitmap = loadImage(name);
		int width = (int) (bitmap.getWidth() * scale);
		int height = (int) (bitmap.getHeight() * scale);
		return Bitmap.createScaledBitmap(bitmap, width, height, false);
	}

	public void deleteFolder(String folder)
	{
		deleteFolder(getFile(folder));
	}

	private void deleteFolder(File folder)
	{
		for (File file : folder.listFiles())
			if (file.isDirectory())
				deleteFolder(file);
			else
				file.delete();
		folder.delete();
	}
	
	@Override
	public String toString()
	{
		return prefix;
	}
	
}
