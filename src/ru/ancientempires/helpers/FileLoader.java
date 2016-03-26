package ru.ancientempires.helpers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.client.Client;
import ru.ancientempires.client.IClientHelper;
import ru.ancientempires.images.ImagesLoader;

public class FileLoader
{
	
	public AssetsHelper	assets;
	public File			baseDirectory;
	public String		prefix;
						
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
	
	public JsonReader getReader(String name) throws IOException
	{
		if (getFile(name).exists())
			return new JsonReader(new FileReader(getFile(name)));
		else
			return new JsonReader(new InputStreamReader(openIS(name)));
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
	
	public OutputStream openOSAppend(String name) throws IOException
	{
		return new FileOutputStream(getFile(name), true);
	}
	
	public DataOutputStream openDOS(String name) throws IOException
	{
		return new DataOutputStream(openOS(name));
	}
	
	public JsonWriter getWriter(String name) throws IOException
	{
		getFile(name).getParentFile().mkdirs();
		return new JsonWriter(new OutputStreamWriter(new FileOutputStream(getFile(name))));
	}
	
	public FileLoader getLoader(String name)
	{
		return new FileLoader(this, name);
	}
	
	public ImagesLoader getImagesLoader()
	{
		return new ImagesLoader(this);
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
	
	@Override
	public String toString()
	{
		return prefix;
	}
	
}
