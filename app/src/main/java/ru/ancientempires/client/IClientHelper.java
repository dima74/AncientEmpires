package ru.ancientempires.client;

import java.io.File;

import ru.ancientempires.helpers.AssetsHelper;

public interface IClientHelper {

	File getFilesDir();

	AssetsHelper getAssets();

	String getID();
}
