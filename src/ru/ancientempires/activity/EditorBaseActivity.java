package ru.ancientempires.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import ru.ancientempires.MenuActions;

public class EditorBaseActivity extends BaseListActivity
{
	
	private static MenuActions[] actions = new MenuActions[]
	{
		MenuActions.CREATE_GAME,
		MenuActions.EDIT_GAME,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, actions);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		moveTo(EditorChooseActivity.class);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (actions[position])
		{
			case CREATE_GAME:
				moveTo(EditorChooseActivity.class);
				break;
			default:
				break;
		}
	}
	
}
