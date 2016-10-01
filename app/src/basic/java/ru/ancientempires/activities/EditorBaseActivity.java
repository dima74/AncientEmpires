package ru.ancientempires.activities;

import android.view.View;
import android.widget.ListView;

import ru.ancientempires.MenuActions;

public class EditorBaseActivity extends BaseListActivity {

	@Override
	public MenuActions[] getStrings() {
		return new MenuActions[]
				{
						MenuActions.CREATE_GAME,
						MenuActions.EDIT_GAME,
				};
	}

	@Override
	public void onLoadFinished() {
		super.onLoadFinished();
		moveTo(EditorConfigureActivity.class);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (getStrings()[position]) {
			case CREATE_GAME:
				moveTo(EditorConfigureActivity.class);
				break;
			default:
				break;
		}
	}

}
