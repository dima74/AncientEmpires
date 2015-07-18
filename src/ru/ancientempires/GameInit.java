package ru.ancientempires;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.AZipHelper;
import ru.ancientempires.images.Images;
import ru.ancientempires.view.draws.GameDrawAction;
import ru.ancientempires.view.draws.Paints;
import android.os.AsyncTask;

public class GameInit
{
	
	public static AsyncTask<Void, Void, Void>	initAsyncTask;
	public static AsyncTask<Void, Void, Void>	gameInitAsyncTask;
	
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
					// Debug.startMethodTracing("traces/client");
					Client.init();
					// Debug.stopMethodTracing();
					
					Client.setImagesZipFile(AZipHelper.getZipFileFromAssets("images.zip"));
					// Debug.startMethodTracing("traces/images");
					Images.preloadResources(Client.imagesZipFile);
					// Debug.stopMethodTracing();
					
					// Debug.startMethodTracing("traces/init");
					GameDrawAction.initResources();
					Paints.init();
					// Debug.stopMethodTracing();
				}
				catch (IOException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				return null;
			}
			
		}.execute();
	}
	
}
