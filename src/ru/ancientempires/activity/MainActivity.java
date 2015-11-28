package ru.ancientempires.activity;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.ancientempires.ALog;
import ru.ancientempires.GameInit;
import ru.ancientempires.Localization;
import ru.ancientempires.MenuActions;
import ru.ancientempires.R;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.helpers.AssetsHelperAndroid;
import ru.ancientempires.helpers.FileHelper;

public class MainActivity extends ListActivity
{
	
	public static Context		context;
	public static AssetManager	assets;
	public static Resources		resources;
	
	private boolean			isFirstLaunch	= true;
	private MenuActions[]	actions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		MainActivity.context = getBaseContext();
		MainActivity.assets = getAssets();
		MainActivity.resources = getResources();
		
		FileHelper.setBaseDirectory(getFilesDir());
		FileHelper.assets = new AssetsHelperAndroid(getAssets());
		
		MyLog.currLog = new ALog();
		
		try
		{
			Localization.load("strings");
		}
		catch (IOException e1)
		{
			MyAssert.a(false);
			e1.printStackTrace();
		}
		
		actions = new MenuActions[]
		{
				MenuActions.PLAY,
				MenuActions.ONLINE,
				MenuActions.SETTINGS,
				MenuActions.MAP_EDITOR,
				MenuActions.INSTRUCTIONS,
				MenuActions.AUTHORS
		};
		
		setContentView(R.layout.main_menu_list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_menu_list_item, R.id.text_view, MenuActions.convertToNames(actions));
		setListAdapter(adapter);
		
		GameInit.init();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (actions[position])
		{
			case PLAY:
				startActivity(new Intent(this, PlayMenuActivity.class));
				break;
				
			default:
				break;
		}
	}
	
}
