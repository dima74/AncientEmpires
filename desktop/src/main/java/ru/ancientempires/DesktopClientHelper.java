package ru.ancientempires;

import java.io.File;

import ru.ancientempires.client.IClientHelper;
import ru.ancientempires.helpers.AssetsHelper;

public class DesktopClientHelper implements IClientHelper {

	@Override
	public File getFilesDir() {
		return new File("app/src/main/assets/");
		// return new File(System.getenv("appdata") + "\\Ancient Empires\\");
	}

	@Override
	public AssetsHelper getAssets() {
		return new AssetsHelper();
	}

	@Override
	public String getID() {
		return "null";
	}

}
