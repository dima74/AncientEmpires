package ru.ancientempires.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ru.ancientempires.GameInit;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.graphics.RippleDrawable;
import ru.ancientempires.images.Images;
import ru.ancientempires.load.GameLoader;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

public class LevelMenuActivity extends Activity
{
	
	private ListView	listView;
	public String		names[];
	
	public GamesFolder	gamesFolder;
	private boolean		isStartGame	= false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_menu);
		
		this.listView = (ListView) findViewById(R.id.listview);
		this.listView.setDividerHeight(0);
		
		LayoutParams layoutParams = (LayoutParams) this.listView.getLayoutParams();
		layoutParams.gravity = Gravity.CENTER_VERTICAL;
		this.listView.setLayoutParams(layoutParams);
		
		setContent(GameLoader.gamesFolder);
		
		BaseAdapter adapter = new BaseAdapter()
		{
			@Override
			public int getCount()
			{
				return LevelMenuActivity.this.names.length;
			}
			
			@Override
			public Object getItem(int position)
			{
				return null;
			}
			
			@Override
			public long getItemId(int position)
			{
				return position;
			}
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent)
			{
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.button_layout, parent, false);
				Button button = (Button) view.findViewById(R.id.button);
				button.setText(LevelMenuActivity.this.names[position]);
				button.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						LevelMenuActivity.this.onClick(position);
					}
				});
				RippleDrawable.createRipple(button, 0xFF00FF00);
				return view;
			}
		};
		
		this.listView.setAdapter(adapter);
		
		// debug
		GamePath gamePath = this.gamesFolder.gamesFolders[1].gamePaths[5];
		startGame(gamePath);
	}
	
	private void setContent(GamesFolder gamesFolder)
	{
		this.gamesFolder = gamesFolder;
		this.names = getNames(gamesFolder);
	}
	
	private void updateContent(GamesFolder gamesFolder)
	{
		setContent(gamesFolder);
		((BaseAdapter) this.listView.getAdapter()).notifyDataSetChanged();
	}
	
	private String[] getNames(GamesFolder gamesFolder)
	{
		ArrayList<String> names = new ArrayList<String>();
		for (GamesFolder newGamesFolder : gamesFolder.gamesFolders)
			names.add(newGamesFolder.path.replace("/", ""));
		for (GamePath newGamePath : gamesFolder.gamePaths)
			names.add(newGamePath.path.replace("/", ""));
		
		return names.toArray(new String[0]);
	}
	
	protected void onClick(int position)
	{
		final GamesFolder[] gamesFolders = this.gamesFolder.gamesFolders;
		if (position < gamesFolders.length)
		{
			updateContent(gamesFolders[position]);
			if (this.names.length == 0)
				Toast.makeText(this, "Здесь пока ничего нет", Toast.LENGTH_LONG).show();
		}
		else
			startGame(this.gamesFolder.gamePaths[position - gamesFolders.length]);
	}
	
	private void startGame(final GamePath gamePath)
	{
		if (this.isStartGame)
			return;
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.loading));
		progressDialog.show();
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				checkInit();
				Client.getClient().startGame(gamePath);
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
				GameActivity.isNewGame = true;
				Intent intent = new Intent();
				intent.setClass(LevelMenuActivity.this, GameActivity.class);
				startActivity(intent);
				LevelMenuActivity.this.isStartGame = false;
				progressDialog.dismiss();
			};
		}.execute();
	}
	
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
	
}
