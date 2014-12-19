package ru.ancientempires.activity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import ru.ancientempires.GameInit;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.ALog;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.Images;
import ru.ancientempires.load.GameLoader;
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
import android.widget.Toast;

public class MainActivity extends Activity
{
	
	public static Context		context;
	public static AssetManager	assets;
	public static Resources		resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		
		MainActivity.context = getBaseContext();
		MainActivity.assets = getAssets();
		MainActivity.resources = getResources();
		
		/*
		Button buttonCampaign = (Button) findViewById(R.id.button_campaign);
		RippleDrawable.createRipple(buttonCampaign, 0xff00ffff);
		buttonCampaign.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startCampaign();
			}
		});
		
		Button buttonSingleplayer = (Button) findViewById(R.id.button_singleplayer);
		RippleDrawable.createRipple(buttonSingleplayer, 0xff00ffff);
		buttonSingleplayer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startSinglePlayer();
			}
		});
		
		Button buttonMultiplayer = (Button) findViewById(R.id.button_multiplayer);
		RippleDrawable.createRipple(buttonMultiplayer, 0xff00ffff);
		buttonMultiplayer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startMultiplayer();
			}
		});
		*/
		
		// мой код
		MyLog.currLog = new ALog();
		
		long s = System.nanoTime();
		GameInit.init();
		long e = System.nanoTime();
		// startCampaign();
		
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
		}, 0, 100);
	}
	
	public static int	amn	= 0;
	
	private void checkInit()
	{
		try
		{
			GameInit.initAsyncTask.get();
		}
		catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean	isStartCampaign	= false;
	
	protected void startCampaign()
	{
		if (this.isStartCampaign)
			return;
		this.isStartCampaign = true;
		MyLog.l("MainActivity.startCampaign()");
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
				MainActivity.this.isStartCampaign = false;
			};
		}.execute();
	}
	
	protected void startSinglePlayer()
	{
		Toast.makeText(MainActivity.this, "Скоро", Toast.LENGTH_SHORT).show();
	}
	
	protected void startMultiplayer()
	{
		Toast.makeText(MainActivity.this, "Скоро", Toast.LENGTH_SHORT).show();
	}
	
}
