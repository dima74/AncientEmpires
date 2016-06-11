package ru.ancientempires.helpers;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class AssetsHelper
{
	
	private AssetManager assets;
	
	public AssetsHelper(AssetManager assets)
	{
		this.assets = assets;
	}
	
	public InputStream openIS(String name) throws IOException
	{
		return assets.open(name);
	}
	
	public boolean exists(String name)
	{
		try
		{
			openIS(name).close();
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	public String[] list(String name) throws IOException
	{
		if (name.endsWith("/"))
			name = name.substring(0, name.length() - 1);
		return assets.list(name);
	}
	
}
