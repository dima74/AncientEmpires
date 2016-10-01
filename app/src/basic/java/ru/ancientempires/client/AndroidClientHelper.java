package ru.ancientempires.client;

import android.app.Activity;
import android.provider.Settings.Secure;

import java.io.File;

import ru.ancientempires.helpers.AssetsHelper;

public class AndroidClientHelper implements IClientHelper {

	public Activity activity;

	public AndroidClientHelper(Activity activity) {
		this.activity = activity;
	}

	@Override
	public AssetsHelper getAssets() {
		return new AssetsHelper(activity.getAssets());
	}

	@Override
	public File getFilesDir() {
		return activity.getFilesDir();
	}

	@Override
	public String getID() {
		return Secure.getString(activity.getContentResolver(), Secure.ANDROID_ID);
	}

}
