package ru.ancientempires.activity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ru.ancientempires.GameInit;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.ALog;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.graphics.RippleDrawable;
import ru.ancientempires.view.GameView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity
{
	
	public static Context		context;
	public static AssetManager	assets;
	public static Resources		resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		MainActivity.context = getBaseContext();
		MainActivity.assets = getAssets();
		MainActivity.resources = getResources();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		
		// мой код
		MyLog.currLog = new ALog();
		
		long s = System.nanoTime();
		GameInit.init();
		long e = System.nanoTime();
		// Toast.makeText(MainActivity.context, "" + (e - s) / 1e9, Toast.LENGTH_LONG).show();
	}
	
	public static int	amn	= 0;
	
	protected void checkInit()
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
		MyLog.log("MainActivity.startCampaign()");
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.loading));
		progressDialog.show();
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				checkInit();
				Client.getClient().startGame("first");
				try
				{
					long s = System.nanoTime();
					GameView.startGame(Client.getClient().getGame());
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
