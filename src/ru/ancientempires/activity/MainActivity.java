package ru.ancientempires.activity;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.ancientempires.MenuActions;
import ru.ancientempires.R;
import ru.ancientempires.client.AndroidClientHelper;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;

public class MainActivity extends ListActivity
{
	
	private static MenuActions[] actions = new MenuActions[]
	{
			MenuActions.PLAY,
			MenuActions.ONLINE,
			MenuActions.SETTINGS,
			MenuActions.MAP_EDITOR,
			MenuActions.INSTRUCTIONS,
			MenuActions.AUTHORS
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (Client.client == null)
			try
			{
				Client.client = new Client(new AndroidClientHelper(this));
				Client.client.startLoadParts12();
			}
			catch (IOException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
		Debug.create(this);
		
		setContentView(R.layout.main_menu_list_view);
		setListAdapter(new ArrayAdapter<MenuActions>(this, R.layout.main_menu_list_item, R.id.text_view, MainActivity.actions));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (MainActivity.actions[position])
		{
			case PLAY:
				startActivity(new Intent(this, PlayMenuActivity.class));
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Debug.onStart(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		startActivity(new Intent(this, PlayMenuActivity.class));
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Debug.onStop(this);
	}
	
	/*
	WindowManager.LayoutParams params = new WindowManager.LayoutParams(
			1, 1,
			LayoutParams.TYPE_APPLICATION,
			LayoutParams.FLAG_NOT_FOCUSABLE
					// | LayoutParams.FLAG_NOT_TOUCH_MODAL
					| LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
			PixelFormat.TRANSPARENT);
	View viewOverlay = new View(this);
	WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	windowManager.addView(viewOverlay, params);
	viewOverlay.setOnTouchListener(new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			// MyLog.l("onTouch() " + MotionEvent.actionToString(event.getAction()));
			return false;
		}
	});
	windowManager.removeView(viewOverlay);
	//*/
	
}
