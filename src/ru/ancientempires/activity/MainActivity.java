package ru.ancientempires.activity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import ru.ancientempires.ALog;
import ru.ancientempires.GameInit;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.Images;
import ru.ancientempires.load.GameLoader;

public class MainActivity extends Activity
{
	
	public static Context		context;
	public static AssetManager	assets;
	public static Resources		resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		MainActivity.context = getBaseContext();
		MainActivity.assets = getAssets();
		MainActivity.resources = getResources();
		MyLog.currLog = new ALog();
		
		GameInit.init();
		
		// TODO заменить, чтобы клиент инит сендил мессадж
		final Handler handler = new Handler(new Handler.Callback()
		{
			@Override
			public boolean handleMessage(Message msg)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, LevelMenuActivity.class);
				startActivity(intent);
				return true;
			}
		});
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				if (GameLoader.gamesFolder != null)
				{
					timer.cancel();
					handler.sendMessage(new Message());
				}
			}
		}, 0, 10);
	}
	
	public static int amn = 0;
	
	private void checkInit()
	{
		try
		{
			GameInit.initAsyncTask.get();
		}
		catch (InterruptedException | ExecutionException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
	public boolean isStartCampaign = false;
	
	protected void startCampaign()
	{
		if (isStartCampaign)
			return;
		isStartCampaign = true;
		// MyLog.l("MainActivity.startCampaign()");
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.loading));
		progressDialog.show();
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				checkInit();
				// Client.getClient().startGame("first");
				try
				{
					long s = System.nanoTime();
					Images.loadResources(Client.imagesZipFile, Client.getClient().getGame());
					long e = System.nanoTime();
				}
				catch (IOException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, GameActivity.class);
				startActivity(intent);
				isStartCampaign = false;
			};
		}.execute();
	}
	
}
