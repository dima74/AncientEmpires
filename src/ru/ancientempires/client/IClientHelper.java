package ru.ancientempires.client;

import java.io.File;

import ru.ancientempires.helpers.AssetsHelper;

public interface IClientHelper
{
	
	public File getFilesDir();
	
	public AssetsHelper getAssets();
	
	public String getID();
	
}
