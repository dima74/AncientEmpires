package ru.ancientempires.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import ru.ancientempires.R;
import ru.ancientempires.framework.Debug;

public class BaseListActivity extends ListActivity
{
	
	protected void onCreate(Bundle savedInstanceState, Object[] strings)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_list_view);
		setListAdapter(new ArrayAdapter<Object>(this, R.layout.main_menu_list_item, R.id.text_view, strings));
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Debug.onStart(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Debug.onStop(this);
	}
	
}
