package ru.ancientempires;

import java.io.IOException;

import ru.ancientempires.activity.MainActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.AZipHelper;
import ru.ancientempires.images.Images;
import ru.ancientempires.view.GameView;
import android.os.AsyncTask;

public class GameInit
{
	
	public static AsyncTask<Void, Void, Void>	initAsyncTask;
	
	public static void init()
	{
		GameInit.initAsyncTask = new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				try
				{
					Client.setGamesZipFile(AZipHelper.getZipFileFromAssets("games.zip"));
					Client.setRulesZipFile(AZipHelper.getZipFileFromAssets("rules.zip"));
					Client.init();
					
					Client.setImagesZipFile(AZipHelper.getZipFileFromAssets("images.zip"));
					Images.preloadResources(Client.imagesZipFile);
					GameView.initResources(MainActivity.resources);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				return null;
			};
		}.execute();
	}
}
