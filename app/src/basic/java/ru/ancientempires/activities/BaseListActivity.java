package ru.ancientempires.activities;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;

public abstract class BaseListActivity extends BaseActivity
{
	
	public MyFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Debug.onCreate(this);
		setContentView(R.layout.main_menu_list_view);
		fragment = (MyFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
		try
		{
			loadBackground();
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		onLoadFinished();
	}

	public void onLoadFinished()
	{
		Object[] objects = getStrings();
		String[] strings = new String[objects.length];
		for (int i = 0; i < objects.length; ++i)
			strings[i] = objects[i].toString();
		fragment.setListAdapter(new ArrayAdapter<>(BaseListActivity.this, R.layout.main_menu_list_item, R.id.text_view, strings));
	}

	public abstract Object[] getStrings();

	protected void onListItemClick(ListView listView, View view, int position, long id)
	{
	}

	public void loadBackground() throws Exception
	{
		Client.client.loadPart0();
	}

	public static class MyFragment extends ListFragment
	{
		public void onListItemClick(ListView listView, View view, int position, long id)
		{
			((BaseListActivity) getActivity()).onListItemClick(listView, view, position, id);
		}
	}
	
}
