package ru.ancientempires;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.AZipHelper;
import ru.ancientempires.images.Images;
import android.os.AsyncTask;

public class GameInit
{
	
	public static class MyAsyncTask extends AsyncTask<String, Void, ZipFile>
	{
		@Override
		protected ZipFile doInBackground(String... params)
		{
			try
			{
				ZipFile zipFile = AZipHelper.getZipFileFromAssets(params[0]);
				return zipFile;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}
	}
	
	private static boolean	initGames;
	private static boolean	initRules;
	public static boolean	initClient;
	public static boolean	initImages;
	
	public static void init()
	{
		new MyAsyncTask()
		{
			@Override
			protected void onPostExecute(ZipFile zipFile)
			{
				Client.setGamesZipFile(zipFile);
				GameInit.initGames();
			};
		}.execute("games.zip");
		
		new MyAsyncTask()
		{
			@Override
			protected void onPostExecute(ZipFile zipFile)
			{
				Client.setRulesZipFile(zipFile);
				GameInit.initRules();
			};
		}.execute("rules.zip");
		
		new MyAsyncTask()
		{
			@Override
			protected void onPostExecute(ZipFile zipFile)
			{
				Client.setImagesZipFile(zipFile);
				GameInit.initImages();
			};
		}.execute("images.zip");
	}
	
	protected static void initGames()
	{
		if (GameInit.initRules)
			GameInit.initClient();
		else
			GameInit.initGames = true;
	}
	
	protected static void initRules()
	{
		if (GameInit.initGames)
			GameInit.initClient();
		else
			GameInit.initRules = true;
	}
	
	private static void initClient()
	{
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				try
				{
					Client.init();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				GameInit.initClient = true;
			}
		}.execute();
	}
	
	protected static void initImages()
	{
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				try
				{
					try
					{
						Thread.sleep(2000);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Images.preloadResources(Client.imagesZipFile);
					GameView.initResources(MainActivity.resources);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				GameInit.initImages = true;
			}
		}.execute();
	}
	
}
