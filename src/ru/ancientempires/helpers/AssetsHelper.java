package ru.ancientempires.helpers;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;

public class AssetsHelper
{
	
	private AssetManager assets;
	
	public AssetsHelper(Activity activity)
	{
		assets = activity.getAssets();
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
		return assets.list(name);
	}
	
}
