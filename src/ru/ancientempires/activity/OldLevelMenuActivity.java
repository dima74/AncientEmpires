package ru.ancientempires.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class OldLevelMenuActivity extends ListActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		String[] strings = new String[]
		{
				"1", "2", "3", "5", "4", "7", "6", "8", "9"
		};
		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, strings);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
	}
	
}
