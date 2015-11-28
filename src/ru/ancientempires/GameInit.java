package ru.ancientempires;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.AndroidImageLoader;
import ru.ancientempires.images.CampaignImages;
import ru.ancientempires.images.Images;
import ru.ancientempires.load.GamesFolder;
import ru.ancientempires.view.draws.GameDrawAction;

public class GameInit
{
	
	public static Thread	foldersInitThread;
	public static Thread	initThread;
	
	public static void init()
	{
		GameInit.foldersInitThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Localization.load("games/strings");
					Client.gamesFolder = new GamesFolder("", null);
				}
				catch (IOException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			}
		};
		GameInit.foldersInitThread.start();
		
		GameInit.initThread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					GameInit.foldersInitThread.join();
				}
				catch (InterruptedException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				
				try
				{
					// Debug.startMethodTracing("traces/client");
					Client.init();
					// Debug.stopMethodTracing();
					
					// Debug.startMethodTracing("traces/images");
					Images.preloadResources();
					CampaignImages.images = new CampaignImages<Bitmap>(new AndroidImageLoader());
					// Debug.stopMethodTracing();
					
					// Debug.startMethodTracing("traces/init");
					GameDrawAction.initResources();
					// Debug.stopMethodTracing();
				}
				catch (IOException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			}
		};
		GameInit.initThread.start();
	}
	
}
